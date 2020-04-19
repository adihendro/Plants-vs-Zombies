Welcome to Plants vs Zombies!

Before playing, there are few things to know:
1. This game needs mouse to operate. Make sure you have the right input device.
2. This game equipped with in-game sound effects. Make sure to turn your volume on.

How to run the program:
1. Put all files into 1 folder
2. Open command prompt (Windows) or terminal (macOS) in that folder
3. Compile all .java files with command:
   javac Main.java World.java Actor.java Audio.java Plant.java Zombie.java Player.java Pea.java Sun.java Point.java
4. Run the program with command: java Main
5. Enjoy!

Tips:
After compiling all .java files, you can create a jar file
1. Put PVZ.mf file in the compiled .class folder
2. Create jar file with command:
   jar cmf PVZ.mf PVZ.jar *.class Assets
3. Run the program by simply double clicking PVZ.jar file


About the game:
This game, for most parts, looks like distinguished "Plant vs Zombie". The goal is to protect the house from zombies
invasion. The house is located on the left side of the screen and zombies will come from the right side. Player may
use several kinds of plants to kill and stop zombies from getting into the house. Player wins if all zombies
were killed and no zombie reaches the house. If there is any zombie gets into the house, player will lose.

The game start with 50 sunflower points. Every 5 seconds a sun will fall from the sky. A sun is worth 25 sunflower points.
There will be 20 zombies in the first wave and 30 zombies later, making a total of 50 zombies.

Below are characters inside the game and their statistics:

—Plants—

1. Sunflower 
Produce 1 sun every 10 seconds
price: 50
health: 40

2. Peashooter 
Shoot 1 pea every 2 seconds
price: 100 
health: 40
damage: 6

3. Repeater 
Shoot 2 peas every 2 seconds
price: 150 
health: 60
damage: 5

4. Wallnut
Block zombies
price: 50 
health: 200

5. Cherrybomb
Explode and kill all zombies around
price: 150 
health: 200
damage: Massive
range: 3x3

—Zombies—

1. Normal Zombie
Just a normal zombie
health: 45
damage: 10
speed: 0.3

2. Football Zombie 
Have double protection from helmet
health: 75
damage: 15
speed: 0.55

3. Flying Zombie
Fly through any plant on the field. It can't eat the plant either.
Why? Because it flies!
health: 60
speed: 0.4
