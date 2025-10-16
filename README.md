# SWEN20003 - Project 2

This is my ReadMe.md to introduce my code structure and some important logic.

**Name:** Haochen You \
**Student Number:** 1796977 \
**Username:** haochenyou \
**Email:** haochenyou@student.unimelb.edu.au

## Completed stages


##### Classes

About rooms,there are totally three types of room : PrepareRoom, BattleRoom,
EndRoom(include win and lose).So there are three classes and an interface(Room) on it.
What's more, in order to realize the "pass" logic, I used an enum to assist it.

About Player, there is a class to store all actions and status.

About Messages, I created a class to store them. That is because if you want to change 
the output in a special scene,such as game over, you can just modify the message file.

Enemy types: BulletKin, AshenBulletKin, and KeyBulletKin

Environment objects: Wall, River, Basket, Table, TreasureBox, Doors, Key, and Restart_Area

Projectiles: Bullet (player) and Fireball (enemy)


##### Important logic 1: pass

How to realize the pass logic? All room store the doors they have, all doors object have a RoomType(enum)
member, there is a map in the constructor of the ShadowDungeon which one-to-one correspondence RoomType and Room.
What's more, there is a Room object currentRoom. When there is a clash, I change the currentRoom, so that in next frame
we can change the scene.

##### Important logic 2: clash

I use the bagel.util.Rectangle to realize it. It can produce a "box" by the image
 of the object, and it can check if a collision occurs between two objects. In every frame, I use the currentRoom.clashTest, 
which can test if there is a clash between the player and the objects in the room. This method returns an integer, which can
 be 1,2,3,4,5, with different meanings.

##### Important logic 3: shooting
The shot is built from data already in the loop. Player.shot(...) gates fire with a coolDown vs bulletFreq, then delegates to room.shotBullet(this, input).
In each room’s shotBullet, I compute disX = mouse.x - player.x and disY = mouse.y - player.y, normalise with sqrt(disX*disX + disY*disY), scale by player.getBulletSpeed(),
and push a new Bullet(playerPos, speedX, speedY, player.getHurtPerShot()) into the room’s bullets list. Enemies use the same pattern in BattleRoom.shotFireball: per-enemy coolDown counts down;
when it hits 0, I recompute a normalised vector toward the player and add a Fireball, then reset coolDown to shotSpeed.

##### Important logic 4: damage
All damage comes from collisions resolved where the data already lives. In BattleRoom.projectilesClashTest,
I iterate fireballs/bullets once per frame, remove any that hit closed doors (priDoor/secDoor.clashXxx),
then apply player damage via player.injured(fireball.getDamage()) on fireball.clash(player). Rivers are continuous damage:
clashTest returns 4 when r.clash(player); the main loop responds with player.injured() (uses hurtPerFrame). Health changes immediately update Messege;
if health <= 0, I flip to EndRoom by calling endRoom.setWinFlag(false) and swapping currentRoom.

##### Important logic 5: pause and reset
Pause is a boolean pause toggled by SPACE inside ShadowDungeon.update. When pause == true, I still draw currentRoom and player,
then overlay store.show(); key presses E/L call Store purchase methods directly. Pressing P while paused triggers a full reset path:
I call player.reset(), prepRoom.reset(), roomA.reset(), roomB.reset(), endRoom.reset(), and set currentRoom = prepRoom.
On any room transition I also call currentRoom.clean() to drop lingering projectiles.

## Assumptions
About the shoot of the player, I assume that the player can't shoot continuously.So I
use "waspressed" not "isDown".

Assume that player can shoot and open the store in any room.

