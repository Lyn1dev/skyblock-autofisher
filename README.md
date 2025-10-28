# 🎣 Skyblock AutoFisher

This is a Fabric mod for Minecraft that automates the process of fishing in Hypixel Skyblock. It automatically reels in fish, and can automatically recast the fishing rod, and has a feature to auto kill the sea creatures spawned.

## ❓ FAQ:

-   **How do I use this?** Download the mod from Releases, drop it in your mods folder on Fabric 1.21.5, go ingame and use `/autofisher on` and cast a fishing rod. The mod will reel in for you. For further usage refer to the Commands section.
-   **Why did you make this?** I enjoy coding things and I love automating stuff.
-   **How come it's open source?** I don't play skyblock much anymore, and I don't have much free time anymore to work on this and make it anything really good or impressive, I hope my mod would be helpful to whoever HATES fishing, and I hope that people would come together to improve my mod. Please don't steal my code and sell it for pennies in your shitty discord servers, that would not be very cool.
-   **I am illiterate and I think this is a rat. Is this a rat?** Feel free to look through the source code and decide for yourself :)
-   **You seem like a cool guy, how can I contact you?** https://discord.gg/GfMvFGJSe2

## ✨ Features

-   **Auto Fish:** Fully Undetected, just fishes for you whenever the !!! appears.
-   **Auto Recast:** Risky, wouldn't recommend using while afk.
-   **Fireveil Autokill:** A little risky and unfinished. How to use this? Put your fireveil wand in your hotbar slot 2, and it will auto kill mobs that are pulled up based on chat message of the sea creature spawning. CURRENTLY ONLY WORKING FOR BACKWATER BAYOU.
-   **Humanized Delays:** All the delays are fully humanized with multiple layers of randomization and they're configurable to better mimic human behavior and avoid detection.
-   **Debug Mode:** A debug mode is available which can be used to view the delays between actions.

## 💻 Commands

-   `/autofisher`: Shows the current status of the mod.
-   `/autofisher on/off`: Enables or disables the autofisher.
-   `/autofisher recast on/off`: Enables or disables the auto recast feature.
-   `/autofisher fireveil on/off`: Enables or disables the Fireveil feature.
-   `/autofisher debug on/off`: Enables or disables debug mode.

## 📝 To do list:

-   I have to add more sea creature spawn messages into the sea creature detection, currently no way to do it yourself unless you clone and edit the code. I hardcoded them for some reason which is honestly kinda stupid looking back but it is what it is, maybe in a future commit I will isolate it into a .csv or a .txt file in which the user would be able to edit what sea creatures they want to auto kill. At the moment I've added like all of the backwater bayou mobs hardcoded.
-   Maybe add more kill modes later on instead of just fireveil, but in theory any item that you only need to right click to kill, will work, as long as you put the kill item in your hotbar slot 2.

## 🤝 Contributing

1.  Fork the repository.
2.  Make your changes.
3.  Submit a pull request for me to review.
