# 🧟‍♂️ Plants vs Zombies 2

A Java and **LibGDX** game simulator developed as a project for the **Advanced Programming** course. This project features diverse offline single-player game modes alongside a local client-server multiplayer experience over `localhost`.

---

## 🌟 Key Features

### 🎮 Offline Single-Player
* **4 Main Chapters:** Engaging stages with varied environments and challenges.
* **Beghouled Minigame:** Match-3 plant swapping with authentic PvZ2 mechanics.
* **Vasebreaker Minigame:** Tactical vase-smashing and resource management.

### ⚔️ Asymmetrical PvP Network
* **1v1 Asymmetrical Combat:** One player controls the Zombies while the other defends with Plants.
* **Localhost Networking:** Fast, zero-lag local networking with no external dependencies or third-party software required.

---

## 🛠 Tech Stack

* **Programming Language:** Java 17+
* **Game Framework:** LibGDX (LWJGL3)
* **Architecture:** Client-Server Pattern
* **Build Tool:** Gradle

---

## 🚀 Installation & Running from Source

The project can be launched directly from **IntelliJ IDEA**.

### Prerequisites
* **Java JDK 17** or higher installed.
* **Git** and **IntelliJ IDEA**.

---

### 1. Clone the Repository
```bash
git clone https://github.com/yasin-2k7/plants-vs-zombies-2.git
cd plants-vs-zombies-2
```

---
### 2. Running the Server

The server manages game state and synchronizes clients over `localhost`.


*    1. Open the project in IntelliJ IDEA and wait for Gradle dependencies to load.
 *   2. Navigate to `core/src/main/java/.../models/network`.
  *  3. Open the **`GameServer.java`** class.
   * 4. Click the green ▶️ icon next to the `main` method and select **Run**.
---

### 3. Running the Clients

To play multiplayer, launch two separate client instances connecting to the local server.

*    1. Navigate to the **`lwjgl3`** module.
 *   2. Locate and open **`Lwjgl3Launcher.java`**.
  *  3. Click **Run** to start the first client window.
   * 4. To start the second client: Open the Run/Debug Configurations for `Lwjgl3Launcher`, check **Allow parallel run**, and click **Run** again.

---

## 👥 Contributors

Developed as part of the Advanced Programming course by:

* [@yasin-2k7](https://github.com/yasin-2k7)
* [@HaniyehAkbari](https://github.com/HaniyehAkbari)
* [@GOLI-2007](https://github.com/GOLI-2007)
