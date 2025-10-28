package com.example;

import com.example.autofisher.config.AutoFisherConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import com.example.mixin.client.PlayerInventoryMixin;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ExampleModClient implements ClientModInitializer {

	private static final String FISHING_HOLOGRAM_NAME = "!!!";
	private long lastDetectionTime = 0;
	private static final long DETECTION_COOLDOWN_MS = 1000; // 1 second cooldown for detection
	private static long lastFireveilTime = 0;
	private final Random random = new Random();
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static ScheduledFuture<?> recastTask; // To hold the scheduled recast task
	private static final List<String> fireveilMessages = List.of(
			"You caught a Sea Walker.",
			"You stumbled upon a Sea Guardian.",
			"It looks like you've disrupted the Sea Witch's brewing session. Watch out, she's furious!",
			"You reeled in a Sea Archer.",
			"The Rider of the Deep has emerged.",
			"Huh? A Catfish!",
			"Is this even a fish? It's the Carrot King!",
			"Your Chumcap Bucket trembles, it's an Agarimoo.",
			"Gross! A Sea Leech!",
			"You've discovered a Guardian Defender of the sea.",
			"You have awoken the Deep Sea Protector, prepare for a battle!",
			"The Water Hydra has come to test your strength.",
			"The Loch Emperor arises from the depths.",
			"Is it a frog? Is it a man? Well, yes, sorta, IT'S FROG MAN!!!!!!",
			"A Snapping Turtle is coming your way, and it's ANGRY!",
			"A garish set of tentacles arise. It's a Blue Ringed Octopus!",
			"The water bubbles and froths. A massive form emerges- you have disturbed the Wiki Tiki! You shall pay the price.",
			"An Oasis Rabbit appears from the water.",
			"An Oasis Sheep appears from the water.",
			"A Water Worm surfaces!",
			"A Poisoned Water Worm surfaces!",
			"An Abyssal Miner breaks out of the water!",
			"A leech of the mines surfaces... you've caught a Mithril Grubber.",
			"A leech of the mines surfaces... you've caught a Medium Mithril Grubber.",
			"A leech of the mines surfaces... you've caught a Large Mithril Grubber.",
			"A leech of the mines surfaces... you've caught a Bloated Mithril Grubber.",
			"A Dumpster Diver has emerged from the swamp!",
			"The Trash Gobbler is hungry for you!",
			"The desolate wail of a Banshee breaks the silence.",
			"A swampy mass of slime emerges, the Bayou Sludge!",
			"A long snout breaks the surface of the water. It's an Alligator!",
			"A massive Titanoboa surfaces. It's body stretches as far as the eye can see.",
			"Phew! It's only a Scarecrow.",
			"You hear trotting from beneath the waves, you caught a Nightmare.",
			"It must be a full moon, a Werewolf appears.",
			"The spirit of a long lost Phantom Fisher has come to haunt you.",
			"This can't be! The manifestation of death himself!",
			"Frozen Steve fell into the pond long ago, never to resurface...until now!",
			"It's a snowman! He looks harmless.",
			"The Grinch stole Jerry's Gifts...get them back!",
			"What is this creature!?",
			"You found a forgotten Nutcracker laying beneath the ice.",
			"A Reindrake forms from the depths.",
			"A tiny fin emerges from the water, you've caught a Nurse Shark.",
			"You spot a fin as blue as the water it came from, it's a Blue Shark.",
			"A striped beast bounds from the depths, the wild Tiger Shark!",
			"Hide no longer, a Great White Shark has tracked your scent and thirsts for your blood!",
			"A Flaming Worm surfaces from the depths!",
			"A Lava Blaze has surfaced from the depths!",
			"A Lava Pigman arose from the depths!",
			"Smells of burning. Must be a Fried Chicken.",
			"Trouble's brewing, it's a Fireproof Witch!"
	);

	@Override
	public void onInitializeClient() {
		AutoFisherConfig.load(); // Load config on client initialization

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(literal("autofisher")
					.executes(context -> {
						context.getSource().sendFeedback(Text.literal("AutoFisher is " + (AutoFisherConfig.enabled ? "enabled" : "disabled") + ". Recast is " + (AutoFisherConfig.enableRecast ? "enabled" : "disabled") + ". Debug mode is " + (AutoFisherConfig.debugMode ? "on" : "off") + ". Fireveil is " + (AutoFisherConfig.fireveilEnabled ? "enabled" : "disabled") + "."));
						return 1;
					})
					.then(literal("on")
							.executes(context -> {
								AutoFisherConfig.enabled = true;
								AutoFisherConfig.save();
								context.getSource().sendFeedback(Text.literal("AutoFisher enabled."));
								return 1;
							}))
					.then(literal("off")
							.executes(context -> {
								AutoFisherConfig.enabled = false;
								AutoFisherConfig.save();
								context.getSource().sendFeedback(Text.literal("AutoFisher disabled."));
								return 1;
							}))
					.then(literal("recast")
							.then(literal("on")
									.executes(context -> {
										AutoFisherConfig.enableRecast = true;
										AutoFisherConfig.save();
										context.getSource().sendFeedback(Text.literal("AutoFisher recast enabled."));
										return 1;
									}))
							.then(literal("off")
									.executes(context -> {
										AutoFisherConfig.enableRecast = false;
										AutoFisherConfig.save();
										context.getSource().sendFeedback(Text.literal("AutoFisher recast disabled."));
										return 1;
									})))
					.then(literal("debug")
							.then(literal("on")
									.executes(context -> {
										AutoFisherConfig.debugMode = true;
										AutoFisherConfig.save();
										context.getSource().sendFeedback(Text.literal("AutoFisher debug mode enabled."));
										return 1;
									}))
							.then(literal("off")
									.executes(context -> {
										AutoFisherConfig.debugMode = false;
										AutoFisherConfig.save();
										context.getSource().sendFeedback(Text.literal("AutoFisher debug mode disabled."));
										return 1;
									})))
					.then(literal("fireveil")
							.then(literal("on")
									.executes(context -> {
										AutoFisherConfig.fireveilEnabled = true;
										AutoFisherConfig.save();
										context.getSource().sendFeedback(Text.literal("AutoFisher Fireveil enabled."));
										return 1;
									}))
							.then(literal("off")
									.executes(context -> {
										AutoFisherConfig.fireveilEnabled = false;
										AutoFisherConfig.save();
										context.getSource().sendFeedback(Text.literal("AutoFisher Fireveil disabled."));
										return 1;
									})))
			);
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!AutoFisherConfig.enabled) return; // Only run if mod is enabled

			if (client.world != null && client.player != null) {
				long currentTime = System.currentTimeMillis();
				if (currentTime - lastDetectionTime > DETECTION_COOLDOWN_MS) {
					for (Entity entity : client.world.getEntities()) {
						if (entity instanceof ArmorStandEntity) {
							ArmorStandEntity armorStand = (ArmorStandEntity) entity;
							if (armorStand.hasCustomName() && armorStand.getCustomName().getString().equals(FISHING_HOLOGRAM_NAME)) {
								if (AutoFisherConfig.debugMode) {
									client.player.sendMessage(Text.literal("Fishing hologram detected! Simulating right click..."), false);
								}
								lastDetectionTime = currentTime;

								// Simulate human-like delay before right-clicking (complex randomization: 56-129ms)
								int baseDelay = 56;
								int randomOffset1 = random.nextInt(37); // 0-36ms
								int randomOffset2 = random.nextInt(38); // 0-37ms
								int initialClickDelay = baseDelay + randomOffset1 + randomOffset2; // Total delay will be between 56ms and 129ms

								scheduler.schedule(() -> {
									MinecraftClient.getInstance().execute(() -> {
										if (MinecraftClient.getInstance().interactionManager != null && MinecraftClient.getInstance().player != null) {
											// First right-click to reel in the fish
											MinecraftClient.getInstance().interactionManager.interactItem(MinecraftClient.getInstance().player, Hand.MAIN_HAND);
											if (AutoFisherConfig.debugMode) {
												MinecraftClient.getInstance().player.sendMessage(Text.literal("Right click (reel in) simulated after " + initialClickDelay + "ms."), false);
											}

											// Handle recast logic based on user's refined requirements
											if (AutoFisherConfig.enableRecast) {
												// Generate a random recast delay between 390ms and 980ms
												int randomDelay1 = random.nextInt(200); // 0-199
												int randomDelay2 = random.nextInt(200); // 0-199
												int randomDelay3 = random.nextInt(200); // 0-199
												int finalRecastDelay = 390 + (randomDelay1 + randomDelay2 + randomDelay3) % (980 - 390 + 1); // Scale to 390-980

												// Always schedule a recast if enableRecast is true.
												// This will be the default recast if Fireveil is disabled,
												// or the "just recast" if Fireveil is enabled but no trigger message appears.
												if (AutoFisherConfig.debugMode) {
													client.player.sendMessage(Text.literal("Scheduling initial recast. Fireveil enabled: " + AutoFisherConfig.fireveilEnabled + ". Recast delay: " + finalRecastDelay + "ms."), false);
												}
												recastTask = scheduler.schedule(() -> {
													MinecraftClient.getInstance().execute(() -> {
														if (MinecraftClient.getInstance().interactionManager != null && MinecraftClient.getInstance().player != null) {
															MinecraftClient.getInstance().interactionManager.interactItem(MinecraftClient.getInstance().player, Hand.MAIN_HAND);
															if (AutoFisherConfig.debugMode) {
																client.player.sendMessage(Text.literal("Right click (initial recast) simulated after " + finalRecastDelay + "ms."), false);
															}
														}
													});
												}, finalRecastDelay, TimeUnit.MILLISECONDS);
											} else if (AutoFisherConfig.debugMode) {
												client.player.sendMessage(Text.literal("Recast not scheduled: enableRecast=" + AutoFisherConfig.enableRecast), false);
											}
										}
									});
								}, initialClickDelay, TimeUnit.MILLISECONDS);
								break; // Only log once per detection cycle
							}
						}
					}
				}
			}
		});

	}

	public static void handleChatMessage(Text message) {
		MinecraftClient.getInstance().execute(() -> {
			if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().player != null && AutoFisherConfig.debugMode) {
				MinecraftClient.getInstance().player.sendMessage(Text.literal("DEBUG: handleChatMessage called."), false);
			}

			if (!AutoFisherConfig.enabled || !AutoFisherConfig.fireveilEnabled) {
				return;
			}

			String chatMessage = message.getString();
			String cleanChatMessage = chatMessage.replaceAll("§[0-9a-fk-or]", "").trim();
			boolean triggerFireveil = false;

			if (AutoFisherConfig.debugMode) {
				MinecraftClient.getInstance().player.sendMessage(Text.literal("DEBUG: Raw chat message: '" + chatMessage + "'"), false);
				MinecraftClient.getInstance().player.sendMessage(Text.literal("DEBUG: Clean chat message: '" + cleanChatMessage + "'"), false);
			}

			for (String fireveilTrigger : fireveilMessages) {
				if (cleanChatMessage.equals(fireveilTrigger)) {
					triggerFireveil = true;
					if (AutoFisherConfig.debugMode) {
						MinecraftClient.getInstance().player.sendMessage(Text.literal("DEBUG: Fireveil trigger matched: '" + fireveilTrigger + "'"), false);
					}
					break;
				}
			}

			if (triggerFireveil) {
				triggerFireveilSequence();
			}
		});
	}

	public static void triggerFireveilSequence() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastFireveilTime < 5000) {
			if (AutoFisherConfig.debugMode) {
				MinecraftClient.getInstance().player.sendMessage(Text.literal("Fireveil sequence cancelled, already ran within the last 5 seconds."), false);
			}
			return;
		}
		lastFireveilTime = currentTime;

		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null && client.interactionManager != null) {
			if (AutoFisherConfig.debugMode) {
				client.player.sendMessage(Text.literal("Fireveil trigger message detected! Initiating Fireveil sequence..."), false);
			}

			PlayerInventoryMixin playerInventoryMixin = (PlayerInventoryMixin) client.player.getInventory();
			int originalSlot = playerInventoryMixin.getSelectedSlot();
			Random random = new Random();

			// Delay before switching to slot 2
			int delayToSlot2 = AutoFisherConfig.fireveilDelayToSlot2_base + random.nextInt(AutoFisherConfig.fireveilDelayToSlot2_random);
			scheduler.schedule(() -> {
				client.execute(() -> {
					if (client.player != null && client.interactionManager != null) {
						playerInventoryMixin.setSelectedSlot(1); // Switch to 2nd slot (index 1)
						client.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(1));
						if (AutoFisherConfig.debugMode) {
							client.player.sendMessage(Text.literal("Switched to slot 2 after " + delayToSlot2 + "ms."), false);
						}

						// Delay before right-click
						int delayToRightClick = AutoFisherConfig.fireveilDelayToRightClick_base + random.nextInt(AutoFisherConfig.fireveilDelayToRightClick_random);
						scheduler.schedule(() -> {
							client.execute(() -> {
								if (client.player != null && client.interactionManager != null) {
									client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
									if (AutoFisherConfig.debugMode) {
										client.player.sendMessage(Text.literal("Right-clicked after " + delayToRightClick + "ms."), false);
									}

									// Delay before switching back to original slot
									int delayToOriginalSlot = AutoFisherConfig.fireveilDelayToOriginalSlot_base + random.nextInt(AutoFisherConfig.fireveilDelayToOriginalSlot_random);
									scheduler.schedule(() -> {
										client.execute(() -> {
											if (client.player != null && client.interactionManager != null) {
												playerInventoryMixin.setSelectedSlot(originalSlot);
												client.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(originalSlot));
												if (AutoFisherConfig.debugMode) {
													client.player.sendMessage(Text.literal("Switched back to original slot " + (originalSlot + 1) + " after " + delayToOriginalSlot + "ms."), false);
												}
											}
										});
									}, delayToOriginalSlot, TimeUnit.MILLISECONDS);

									// After Fireveil sequence, schedule the recast if enabled
									if (AutoFisherConfig.enableRecast) {
										if (recastTask != null && !recastTask.isDone()) {
											recastTask.cancel(false);
											if (AutoFisherConfig.debugMode) {
												client.player.sendMessage(Text.literal("Cancelled initial recast task due to Fireveil trigger."), false);
											}
										}

										// Generate a random recast delay between 390ms and 980ms
										int randomDelay1 = random.nextInt(200); // 0-199
										int randomDelay2 = random.nextInt(200); // 0-199
										int randomDelay3 = random.nextInt(200); // 0-199
										int finalRecastDelay = 390 + (randomDelay1 + randomDelay2 + randomDelay3) % (980 - 390 + 1); // Scale to 390-980

										recastTask = scheduler.schedule(() -> {
											MinecraftClient.getInstance().execute(() -> {
												if (MinecraftClient.getInstance().interactionManager != null && MinecraftClient.getInstance().player != null) {
													MinecraftClient.getInstance().interactionManager.interactItem(MinecraftClient.getInstance().player, Hand.MAIN_HAND);
													if (AutoFisherConfig.debugMode) {
														client.player.sendMessage(Text.literal("Right click (recast) simulated after " + finalRecastDelay + "ms."), false);
													}
												}
											});
										}, finalRecastDelay, TimeUnit.MILLISECONDS);
									}
								}
							});
						}, delayToRightClick, TimeUnit.MILLISECONDS);
					}
				});
			}, delayToSlot2, TimeUnit.MILLISECONDS);
		}
	}
}
