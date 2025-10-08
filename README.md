# SWEN20003 - Project 1

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

About other objects(walls,rivers and so on), they have their only class.


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

## Assumptions

There are no any assumptions here, all the results are similar to those in the demonstration video.

