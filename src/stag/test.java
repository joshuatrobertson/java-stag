package stag;

public class test {

    public static void main(String args[])
    {

        GameMain gameMain2 = new GameMain("entities/extended-entities.dot", "actions/extended-actions.json");
        gameMain2.loadGame();
/*
        System.out.println(gameMain2.runCommand("Josh: get axe"));
        System.out.println(gameMain2.runCommand("Josh: look"));
        System.out.println(gameMain2.runCommand("Josh: goto forest"));
        System.out.println(gameMain2.runCommand("Josh: cut tree"));
        System.out.println(gameMain2.runCommand("Josh: goto riverbank"));
        System.out.println(gameMain2.runCommand("Josh: bridge river"));
        System.out.println(gameMain2.runCommand("Josh: look"));
        System.out.println(gameMain2.runCommand("Josh: goto riverbank"));

        System.out.println(gameMain2.runCommand("paul: look"));
        System.out.println(gameMain2.runCommand("Josh: goto forest"));
       System.out.println(gameMain2.runCommand("Josh: goto riverbank"));
*/



        System.out.println(gameMain2.runCommand("Josh: look"));

        System.out.println(gameMain2.runCommand("Josh: get axe"));
        System.out.println(gameMain2.runCommand("Josh: goto forest"));
        System.out.println(gameMain2.runCommand("Josh: cut tree"));
        System.out.println(gameMain2.runCommand("Josh: get key"));
        System.out.println(gameMain2.runCommand("Josh: goto cabin"));
        System.out.println(gameMain2.runCommand("Josh: open the big large trapdoor"));
        System.out.println(gameMain2.runCommand("Josh: look"));
        System.out.println(gameMain2.runCommand("Josh: fight elf"));
        System.out.println(gameMain2.runCommand("Josh: fight elf"));
        System.out.println(gameMain2.runCommand("Josh: fight elf"));
        System.out.println(gameMain2.runCommand("Josh: look"));


        System.out.println(gameMain2.runCommand("Josh: random words"));









    }
}
