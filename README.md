# Skyblock AutoFisher

This is a Fabric mod for Minecraft that automates the process of fishing in Hypixel Skyblock. It automatically reels in fish and recasts the fishing rod, with a special "Fireveil" feature to handle the Fireveil fishing rod.

## Features

-   **Auto Recast:** Automatically recasts the fishing rod after a fish is caught.
-   **Fireveil Support:** Includes a special sequence to handle the Fireveil fishing rod, which is triggered by specific chat messages.
-   **Configurable Delays:** All delays are configurable to better mimic human behavior and avoid detection.
-   **Debug Mode:** A debug mode is available to help with troubleshooting.

## Commands

-   `/autofisher`: Shows the current status of the mod.
-   `/autofisher on/off`: Enables or disables the entire mod.
-   `/autofisher recast on/off`: Enables or disables the auto recast feature.
-   `/autofisher fireveil on/off`: Enables or disables the Fireveil support.
-   `/autofisher debug on/off`: Enables or disables debug mode.

## Configuration

The mod can be configured by editing the `autofisher.properties` file in the Minecraft directory. The following properties are available:

-   `enabled`: `true` or `false`
-   `enableRecast`: `true` or `false`
-   `debugMode`: `true` or `false`
-   `fireveilEnabled`: `true` or `false`
-   `fireveilDelayToSlot2_base`: The base delay before switching to slot 2 (in milliseconds).
-   `fireveilDelayToSlot2_random`: The random component of the delay before switching to slot 2.
-   `fireveilDelayToRightClick_base`: The base delay before right-clicking.
-   `fireveilDelayToRightClick_random`: The random component of the delay before right-clicking.
-   `fireveilDelayToOriginalSlot_base`: The base delay before switching back to the original slot.
-   `fireveilDelayToOriginalSlot_random`: The random component of the delay before switching back to the original slot.

## Building from Source

1.  Clone the repository.
2.  Run `gradlew build` in the project directory.
3.  The compiled `.jar` file will be in the `build/libs` directory.
