package test;
import stag.GameMain;
import org.junit.jupiter.api.*;

public class BlackBoxTesting {

    GameMain gm;
    String output;

    @BeforeEach
    public void SetUp() {
        gm = new GameMain("entities/extended-entities.dot", "actions/extended-actions.json");
        gm.loadGame();
    }

    @Test
    @DisplayName("Black box testing")
    public void blackBoxTesting() {
        // look and get commands
        output = gm.runCommand("Josh: look");
        Assertions.assertTrue(output.contains("A log cabin"), output);
        output = gm.runCommand("Josh: get axe");
        Assertions.assertTrue(output.contains("picked up a axe"), output);
        output = gm.runCommand("Josh: get axe");
        Assertions.assertFalse(output.contains("picked up a axe"), output);

        // Inventory command
        output = gm.runCommand("Josh: inv");
        Assertions.assertTrue(output.contains("axe"), output);

        output = gm.runCommand("Josh: inventory");
        Assertions.assertTrue(output.contains("axe"), output);

        // New person
        output = gm.runCommand("Peter: get axe");
        Assertions.assertFalse(output.contains("picked up a axe"), output);

        // goto
        output = gm.runCommand("Josh: goto forest");
        Assertions.assertTrue(output.contains("You are in A deep dark forest"), output);

        // New person should not have moved
        output = gm.runCommand("Peter: look");
        Assertions.assertTrue(output.contains("You are in A log cabin"), output);

        // Josh get key
        output = gm.runCommand("Josh: get key");
        Assertions.assertTrue(output.contains("picked up a key"), output);

        output = gm.runCommand("Josh: goto cabin");
        Assertions.assertTrue(output.contains("You are in A log cabin"), output);

        // Open
        output = gm.runCommand("Josh: open the big large trapdoor");
        Assertions.assertTrue(output.contains("You unlock the trapdoor and see steps leading down into a cellar"), output);

        // Testing other command
        output = gm.runCommand("Josh: fight the elf");
        Assertions.assertTrue(output.contains("You attack the elf, but he fights back and you lose some health"), output);

        //Testing health command
        output = gm.runCommand("Josh: health");
        Assertions.assertTrue(output.contains("2"), output);

        //Testing player dead command
        output = gm.runCommand("Josh: fight the elf");
        Assertions.assertTrue(output.contains("You attack the elf, but he fights back and you lose some health"), output);
        output = gm.runCommand("Josh: fight the elf");
        Assertions.assertTrue(output.contains("You have lost all of your items and are now back at the start"), output);

        // Check back at starting location
        output = gm.runCommand("Josh: look");
        Assertions.assertTrue(output.contains("You are in A log cabin"), output);

        // Check that items were dropped at the correct location
        output = gm.runCommand("Peter: goto cellar");
        Assertions.assertTrue(output.contains("You are in A dusty cellar"), output);
        output = gm.runCommand("Peter: look");
        Assertions.assertTrue(output.contains("axe"), output);




/*
        You attack the elf, but he fights back and you lose some health
        You attack the elf, but he fights back and you lose some health
        You attack the elf, but he fights back and you lose some health
        You attack the elf, but he fights back and you lose some health
        You have lost all of your items and are now back at the start
        You are in A log cabin in the woods. You can see:
        A bottle of magic potion
        A silver coin
        A locked wooden trapdoor in the floor
        You can access from here:
        forest
                cellar

        You cannot do that
*/


    }



}
