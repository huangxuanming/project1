import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.SwitchPoint;
import java.util.Scanner;

/**
 * Created by lenovo on 2016/10/10.
 */
public class AnimalChecker {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("指令介绍：" + "\n    " + "\n    1.移动指令：" + "\n    移动指令是由两部分组成的。" +
                "\n    第一部分是由数字1-8，根据战斗力的分别对应鼠（1），猫(2),狼（3），狗（4），豹（5），虎（6），狮(7),象（8）" +
                "\n    第二部分是数字wasd中的一个，w对应上方向，a对应左方向，s对应下方向，d对应右方向" +
                "\n    比如指令“1d”表示鼠向右走，“4w”表示狗向左走" +
                "\n    " +
                "\n    2.游戏指令：" +
                "\n    输入restart重新开始游戏" +
                "\n    输入help查看帮助" +
                "\n    输入undo悔棋" +
                "\n    输入redo取消悔棋" +
                "\n    输入exit退出游戏");
        Scanner scanner = new Scanner(new File("tile.txt"));
        Scanner input = new Scanner(new File("animal.txt"));
        char[][] tile = new char[7][9];
        char[][] animal = new char[7][9];
        char[][][] mapHistory = new char[500][][];
        for (int i = 0; i < 7; i++) {
            String line1 = scanner.nextLine();
            String line2 = input.nextLine();
            for (int j = 0; j < 9; j++) {
                tile[i][j] = line1.charAt(j);
                animal[i][j] = line2.charAt(j);
            }
        }
        Scanner input1 = new Scanner(System.in);
        mapHistory[0] = copyBoard(animal);
        outputmap(animal, tile);
        int currentStep = 1;
        int lastStep = 1;
        b:
        while (currentStep < 500) {
            //currentStep为奇数时该左方行动；为偶数时该右方行动。
            if (currentStep % 2 == 1)
                System.out.print("请左方玩家行动：");
            else System.out.print("请右方玩家行动：");
            String order = input1.next();
            if (order.equals("help")) {
                System.out.println("指令介绍：" + "\n    " + "\n    1.移动指令：" + "\n    移动指令是由两部分组成的。" +
                        "\n    第一部分是由数字1-8，根据战斗力的分别对应鼠（1），猫(2),狼（3），狗（4），豹（5），虎（6），狮(7),象（8）" +
                        "\n    第二部分是数字wasd中的一个，w对应上方向，a对应左方向，s对应下方向，d对应右方向" +
                        "\n    比如指令“1d”表示鼠向右走，“4w”表示狗向左走" +
                        "\n    " +
                        "\n    2.游戏指令：" +
                        "\n    输入restart重新开始游戏" +
                        "\n    输入help查看帮助" +
                        "\n    输入undo悔棋" +
                        "\n    输入redo取消悔棋" +
                        "\n    输入exit退出游戏");
                currentStep --;
            } else if (order.length() == 2) {//输入的是移动指令,可以进行落子。
                char num = order.charAt(0);
                char direction = order.charAt(1);
                if (num < '1' || num > '8') {
                    System.out.println("你控制的不是动物哦，请重新输入：");
                    continue b;
                } else {
                    if (direction != 'w' && direction != 'a' && direction != 's' && direction != 'd') {
                        System.out.println("你输入的指令不能让你的动物正确移动哦，请重新输入：");
                        continue b;
                    } else {
                        int jump = 0;
                        jump = move(num, direction, animal, tile, currentStep,jump);
                        if (jump == 1)
                            continue b;
                        else mapHistory[currentStep] = copyBoard(animal);
                    }
                }
            } else if (order.equals("undo")) {
                int n = --currentStep;
                if (n > 0) {
                    animal = copyBoard(mapHistory[currentStep-1]);
                    currentStep--;
                }else {
                    System.out.println("您已经在restart了，无法进行悔棋啦。");
                    currentStep ++;
                };
            } else if (order.equals("redo")) {
                if (currentStep < lastStep)
                    animal = copyBoard(mapHistory[currentStep]);
                else System.out.print("您都没有进行悔棋，怎么能进行撤销呢?");
            } else if (order.equals("restart")) {
                System.out.print("您已经重新开始啦！");
                animal = copyBoard(mapHistory[0]);
            } else if (order.equals("exit")) {
                System.out.print("您已经退出游戏啦！");
                break;
            } else {
                System.out.println("你输入的不是合法指令哦，请仔细检查，然后重新输入：");
                continue b;
            }
            lastStep = ++currentStep;
            outputmap(animal, tile);
            //下面进行胜负判断。
            if (animal[3][0] == 'a' || animal[3][0] == 'b' || animal[3][0] == 'c' || animal[3][0] == 'd' || animal[3][0] == 'e' || animal[3][0] == 'f' || animal[3][0] == 'g' || animal[3][0] == 'h') {
                System.out.print("恭喜，右方玩家已经获得了胜利！");
                break b;
            }
            if (animal[3][8] == '1' || animal[3][8] == '2' || animal[3][8] == '3' || animal[3][8] == '4' || animal[3][8] == '5' || animal[3][8] == '6' || animal[3][8] == '7' || animal[3][8] == '8') {
                System.out.print("恭喜，左方玩家已经获得了胜利！");
                break b;
            }
            int number1 = 0;
            int number2 = 0;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    if (animal[i][j] == '1' || animal[i][j] == '2' || animal[i][j] == '3' || animal[i][j] == '4' || animal[i][j] == '5' || animal[i][j] == '6' || animal[i][j] == '7' || animal[i][j] == '8') {
                        number1++;
                    }
                    if (animal[i][j] == 'a' || animal[i][j] == 'b' || animal[i][j] == 'c' || animal[i][j] == 'd' || animal[i][j] == 'e' || animal[i][j] == 'f' || animal[i][j] == 'g' || animal[i][j] == 'h') {
                        number2++;
                    }
                }
            }
            if (number1 == 0) {
                System.out.print("恭喜，右方已经获得了胜利！");
                break b;
            }
            if (number2 == 0) {
                System.out.print("恭喜，左方已经获得了胜利！");
                break b;
            }
            //outputmap(animal, tile);
        }
    }

    public static void outputmap(char[][] a, char[][] b) {
        System.out.println("------------------------------------");
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                switch (a[i][j]) {
                    case '1':
                        System.out.print("1鼠 ");
                        break;
                    case 'a':
                        System.out.print(" 鼠1");//数字的左右用于区分左右选手
                        break;
                    case '2':
                        System.out.print("2猫 ");
                        break;
                    case 'b':
                        System.out.print(" 猫2");
                        break;
                    case '3':
                        System.out.print("3狼 ");
                        break;
                    case 'c':
                        System.out.print(" 狼3");
                        break;
                    case '4':
                        System.out.print("4狗 ");
                        break;
                    case 'd':
                        System.out.print(" 狗4");
                        break;
                    case '5':
                        System.out.print("5豹 ");
                        break;
                    case 'e':
                        System.out.print(" 豹5");
                        break;
                    case '6':
                        System.out.print("6虎 ");
                        break;
                    case 'f':
                        System.out.print(" 虎6");
                        break;
                    case '7':
                        System.out.print("7狮 ");
                        break;
                    case 'g':
                        System.out.print(" 狮7");
                        break;
                    case '8':
                        System.out.print("8象 ");
                        break;
                    case 'h':
                        System.out.print(" 象8");
                        break;
                    case '0':
                        switch (b[i][j]) {
                            case '1':
                                System.out.print(" 水 ");
                                break;
                            case '2':
                            case '4':
                                System.out.print(" 陷 ");
                                break;
                            case '3':
                            case '5':
                                System.out.print(" 家 ");
                                break;
                            case '0':
                                System.out.print("    ");
                        }
                }
            }
            System.out.println();
        }
        System.out.println("------------------------------------");
    }

    public static int move(char num, char direction, char[][] a, char[][] b, int currentStep ,int jump ) {
        if (currentStep % 2 == 1) {//左方玩家行动
            a:
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {//因为鼠、虎、狮、象在下水移动和吃敌的情况比较特殊单独进行考虑。
                    if (num == '1') {
                        if (a[i][j] == num) {//对鼠进行操作。
                            switch (direction) {
                                case 'w':
                                    if (i - 1 < 0) {
                                        System.out.println("您的鼠已经超出地图上边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i - 1][j] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofmouse1(jump, i, j, i - 1, j, num, a, b);
                                    }
                                    break a;
                                case 'a':
                                    if (j - 1 < 0) {
                                        System.out.println("您的鼠已经超出地图左边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j - 1] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofmouse1(jump, i, j, i, j - 1, num, a, b);
                                    }
                                    break a;
                                case 's':
                                    if (i + 1 > 6) {
                                        System.out.println("您的鼠已经超出地图下边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i + 1][j] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofmouse1(jump, i, j, i + 1, j, num, a, b);
                                    }
                                    break a;
                                case 'd':
                                    if (j + 1 > 8) {
                                        System.out.println("您的鼠已经超出地图右边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j + 1] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofmouse1(jump, i, j, i, j + 1, num, a, b);
                                    }
                                    break a;
                            }
                        }
                    }
                    if (num == '8') {
                        if (a[i][j] == num) {//对象进行操作
                            switch (direction) {
                                case 'w':
                                    if (i - 1 < 0) {
                                        System.out.println("您的象已经超出地图上边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i - 1][j] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofelephant1(jump, i, j, i - 1, j, num, a, b);
                                    }
                                    break a;
                                case 'a':
                                    if (j - 1 < 0) {
                                        System.out.println("您的象已经超出地图左边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j - 1] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofelephant1(jump, i, j, i, j - 1, num, a, b);
                                    }
                                    break a;
                                case 's':
                                    if (i + 1 > 6) {
                                        System.out.println("您的象已经超出地图下边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i + 1][j] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofelephant1(jump, i, j, i + 1, j, num, a, b);
                                    }
                                    break a;
                                case 'd':
                                    if (j + 1 > 8) {
                                        System.out.println("您的象已经超出地图右边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j + 1] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofelephant1(jump, i, j, i, j + 1, num, a, b);
                                    }
                                    break a;
                            }
                        }
                    }
                    if (num == '6' || num == '7') {
                        if (a[i][j] == num) {//虎和狮可以跳过河，单独考虑。
                            switch (direction) {
                                case 'w':
                                    if (i - 1 < 0) {
                                        System.out.println("您的兽已经超出地图上边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i - 1][j] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveoftigerandlone1(jump, i, j, i - 1, j, i - 3, j, num, a, b);
                                    }
                                    break a;
                                case 'a':
                                    if (j - 1 < 0) {
                                        System.out.println("您的兽已经超出地图左边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j - 1] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveoftigerandlone1(jump, i, j, i, j - 1, i, j - 4, num, a, b);
                                    }
                                    break a;
                                case 's':
                                    if (i + 1 > 6) {
                                        System.out.println("您的兽已经超出地图下边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i + 1][j] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveoftigerandlone1(jump, i, j, i + 1, j, i + 3, j, num, a, b);
                                    }
                                    break a;
                                case 'd':
                                    if (j + 1 > 8) {
                                        System.out.println("您的兽已经超出地图右边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j + 1] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveoftigerandlone1(jump, i, j, i, j + 1, i, j + 4, num, a, b);
                                    }
                                    break a;
                            }
                        }
                    }
                    if (num >= '2' && num <= '5') {
                        if (a[i][j] == num) {//最普通的情况。
                            switch (direction) {
                                case 'w':
                                    if (i - 1 < 0) {
                                        System.out.println("你的兽已经超出地图的上边界，请重新输入：");
                                        jump ++;
                                    } else {if (b[i - 1][j] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofcommon1(jump, i, j, i - 1, j, num, a, b);
                                    }
                                    break a;
                                case 'a':
                                    if (j - 1 < 0) {
                                        System.out.println("你的兽已经超出地图的左边界，请重新输入：");
                                        jump ++;
                                    } else {if (b[i][j - 1] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofcommon1(jump, i, j, i, j - 1, num, a, b);
                                    }
                                    break a;
                                case 's':
                                    if (i + 1 > 6) {
                                        System.out.println("你的兽已经超出地图的下边界，请重新输入：");
                                        jump ++;
                                    } else {if (b[i + 1][j] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofcommon1(jump, i, j, i + 1, j, num, a, b);
                                    }
                                    break a;
                                case 'd':
                                    if (j + 1 > 8) {
                                        System.out.println("你的兽已经超出地图的右边界，请重新输入：");
                                        jump ++;
                                    } else {if (b[i][j + 1] == '3'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofcommon1(jump, i, j, i, j + 1, num, a, b);
                                    }
                                    break a;
                            }
                        }
                    }
                }
            }
        } else {//玩家为右方时的情况。
            a:
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {//因为鼠、虎、狮、象在下水移动和吃敌的情况比较特殊单独进行考虑。
                    if (num == '1') {
                        if (a[i][j] == (char) (num + 48)) {//对鼠进行操作。
                            switch (direction) {
                                case 'w':
                                    if (i - 1 < 0) {
                                        System.out.println("您的鼠已经超出地图上边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i - 1][j] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofmouse2(jump, i, j, i - 1, j, num, a, b);
                                    }
                                    break a;
                                case 'a':
                                    if (j - 1 < 0) {
                                        System.out.println("您的鼠已经超出地图左边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j - 1] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofmouse2(jump, i, j, i, j - 1, num, a, b);
                                    }
                                    break a;
                                case 's':
                                    if (i + 1 > 6) {
                                        System.out.println("您的鼠已经超出地图下边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i + 1][j] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofmouse2(jump, i, j, i + 1, j, num, a, b);
                                    }
                                    break a;
                                case 'd':
                                    if (j + 1 > 8) {
                                        System.out.println("您的鼠已经超出地图右边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b [i][j + 1] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofmouse2(jump, i, j, i, j + 1, num, a, b);
                                    }
                                    break a;
                            }
                        }
                    }
                    if (num == '8') {
                        if (a[i][j] == (char) (num + 48)) {//对象进行操作
                            switch (direction) {
                                case 'w':
                                    if (i - 1 < 0) {
                                        System.out.println("您的象已经超出地图上边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i - 1][j] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofelephant2(jump, i, j, i - 1, j, num, a, b);
                                    }
                                    break a;
                                case 'a':
                                    if (j - 1 < 0) {
                                        System.out.println("您的象已经超出地图左边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j - 1] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofelephant2(jump, i, j, i, j - 1, num, a, b);
                                    }
                                    break a;
                                case 's':
                                    if (i + 1 > 6) {
                                        System.out.println("您的象已经超出地图下边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i + 1][j] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofelephant2(jump, i, j, i + 1, j, num, a, b);
                                    }
                                    break a;
                                case 'd':
                                    if (j + 1 > 8) {
                                        System.out.println("您的象已经超出地图右边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j + 1] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofelephant2(jump, i, j, i, j + 1, num, a, b);
                                    }
                                    break a;
                            }
                        }
                    }
                    if (num == '6' || num == '7') {
                        if (a[i][j] == (char) (num + 48)) {//虎和狮可以跳过河，单独考虑。
                            switch (direction) {
                                case 'w':
                                    if (i - 1 < 0) {
                                        System.out.println("您的兽已经超出地图上边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i - 1][j] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveoftigerandlone2(jump, i, j, i - 1, j, i - 3, j, num, a, b);
                                    }
                                    break a;
                                case 'a':
                                    if (j - 1 < 0) {
                                        System.out.println("您的兽已经超出地图左边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j - 1] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveoftigerandlone2(jump, i, j, i, j - 1, i, j - 4, num, a, b);
                                    }
                                    break a;
                                case 's':
                                    if (i + 1 > 6) {
                                        System.out.println("您的兽已经超出地图下边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i + 1][j] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveoftigerandlone2(jump, i, j, i + 1, j, i + 3, j, num, a, b);
                                    }
                                    break a;
                                case 'd':
                                    if (j + 1 > 8) {
                                        System.out.println("您的兽已经超出地图右边界了哦，请重新输入指令：");
                                        jump ++;
                                    } else {if (b[i][j + 1] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveoftigerandlone2(jump, i, j, i, j + 1, i, j + 4, num, a, b);
                                    }
                                    break a;
                            }
                        }
                    }
                    if (num >= '2' && num <= '5') {
                        if (a[i][j] == (char) (num + 48)) {//最普通的情)况。
                            switch (direction) {
                                case 'w':
                                    if (i - 1 < 0) {
                                        System.out.println("你的兽已经超出地图的上边界，请重新输入：");
                                        jump ++;
                                    } else {if (b[i - 1][j] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofcommon2(jump, i, j, i - 1, j, num, a, b);
                                    }
                                    break a;
                                case 'a':
                                    if (j - 1 < 0) {
                                        System.out.println("你的兽已经超出地图的左边界，请重新输入：");
                                        jump ++;
                                    } else {if (b[i][j - 1] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofcommon2(jump, i, j, i, j - 1, num, a, b);
                                    }
                                    break a;
                                case 's':
                                    if (i + 1 > 6) {
                                        System.out.println("你的兽已经超出地图的下边界，请重新输入：");
                                        jump ++;
                                    } else {if (b[i + 1][j] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofcommon2(jump, i, j, i + 1, j, num, a, b);
                                    }
                                    break a;
                                case 'd':
                                    if (j + 1 > 8) {
                                        System.out.println("你的兽已经超出地图的右边界，请重新输入：");
                                        jump ++;
                                    } else {if (b[i][j + 1] == '5'){
                                        System.out.println("您的动物不能回自己的家哦，请重新输入：");
                                        jump ++;
                                    }else jump = moveofcommon2(jump, i, j, i, j + 1, num, a, b);
                                    }
                                    break a;
                            }
                        }
                    }
                }
            }
        }
        return jump;
    }

    public static int moveofmouse1(int jump, int now1, int now2, int next1, int next2, char num, char[][] a, char[][] b) {//用于左方老鼠的移动。
        if (b[next1][next2] == '2') {//在己方的陷阱中，无论是对方什么动物，都可以吃掉。
            a[next1][next2] = num;
            a[now1][now2] = '0';
        } else {
            if (a[next1][next2] == '1' || a[next1][next2] == '2' || a[next1][next2] == '3' || a[next1][next2] == '4' || a[next1][next2] == '5' || a[next1][next2] == '6' || a[next1][next2] == '7' || a[next1][next2] == '8') {
                System.out.println("都是一个对的，为什么要互相残杀呢？请重新输入：");
                jump ++;
            } else {
                if (a[next1][next2] == 'b' || a[next1][next2] == 'c' || a[next1][next2] == 'd' || a[next1][next2] == 'e' || a[next1][next2] == 'f' || a[next1][next2] == 'g') {
                    System.out.println("您不可以送死哦，请重新输入：");
                    jump ++;
                } else {
                    if (a[next1][next2] == 'a') {
                        a[next1][next2] = num;
                        a[now1][now2] = '0';
                    } else {
                        if (a[next1][next2] == 'h') {
                            if (b[now1][now2] == '1') {
                                System.out.println("水中的鼠不能吃象哦，请重新输入：");
                                jump ++;
                            } else {
                                a[next1][next2] = num;
                                a[now1][now2] = '0';
                            }
                        } else {
                            a[next1][next2] = num;
                            a[now1][now2] = '0';
                        }
                    }
                }
            }
        }
        return jump;
    }

    public static int moveofmouse2(int jump, int now1, int now2, int next1, int next2, char num, char[][] a, char[][] b) {//用于右方老鼠的移动。
        if (b[next1][next2] == '4') {//在己方的陷阱中，无论是对方什么动物，都可以吃掉。
            a[next1][next2] = (char) (num + 48);
            a[now1][now2] = '0';
        } else {
            if (a[next1][next2] == 'a' || a[next1][next2] == 'b' || a[next1][next2] == 'c' || a[next1][next2] == 'd' || a[next1][next2] == 'e' || a[next1][next2] == 'f' || a[next1][next2] == 'g' || a[next1][next2] == 'h') {
                System.out.println("都是一个对的，为什么要互相残杀呢？请重新输入：");
                jump ++;
            } else {
                if (a[next1][next2] == '2' || a[next1][next2] == '3' || a[next1][next2] == '4' || a[next1][next2] == '5' || a[next1][next2] == '6' || a[next1][next2] == '7') {
                    System.out.println("您不可以送死哦，请重新输入：");
                    jump ++;
                } else {
                    if (a[next1][next2] == '1') {
                        a[next1][next2] = (char) (num + 48);
                        a[now1][now2] = '0';
                    } else {
                        if (a[next1][next2] == '8') {
                            if (b[now1][now2] == '1') {
                                System.out.println("水中的鼠不能吃象哦，请重新输入：");
                                jump ++;
                            } else {
                                a[next1][next2] = (char) (num + 48);
                                a[now1][now2] = '0';
                            }
                        } else {
                            a[next1][next2] = (char) (num + 48);
                            a[now1][now2] = '0';
                        }
                    }
                }
            }
        }
        return jump;
    }

    public static int moveofelephant1(int jump, int now1, int now2, int next1, int next2, char num, char[][] a, char[][] b) {//用于左方大象的移动。
        if (b[next1][next2] == '2') {//在己方的陷阱中，无论是对方什么动物，都可以吃掉。
            a[next1][next2] = num;
            a[now1][now2] = '0';
        } else {
            if (b[next1][next2] == '1') {
                System.out.println("您的象不能下水哦，请重新输入指令：");
                jump ++;
            } else {
                if (a[next1][next2] == '1' || a[next1][next2] == '2' || a[next1][next2] == '3' || a[next1][next2] == '4' || a[next1][next2] == '5' || a[next1][next2] == '6' || a[next1][next2] == '7' || a[next1][next2] == '8') {
                    System.out.println("都是一个对的，为什么要互相残杀呢？请重新输入：");
                    jump ++;
                } else {
                    if (a[next1][next2] == 'a') {
                        System.out.println("您不可以送死哦，请重新输入指令：");
                        jump ++;
                    } else {
                        a[next1][next2] = num;
                        a[now1][now2] = '0';
                    }
                }
            }
        }
        return jump;
    }

    public static int moveofelephant2(int jump, int now1, int now2, int next1, int next2, char num, char[][] a, char[][] b) {//用于右方大象的移动。
        if (b[next1][next2] == '4') {//在己方的陷阱中，无论是对方什么动物，都可以吃掉。
            a[next1][next2] = (char) (num + 48);
            a[now1][now2] = '0';
        } else {
            if (b[next1][next2] == '1') {
                System.out.println("您的象不能下水哦，请重新输入指令：");
                jump ++;
            } else {
                if (a[next1][next2] == 'a' || a[next1][next2] == 'b' || a[next1][next2] == 'c' || a[next1][next2] == 'd' || a[next1][next2] == 'e' || a[next1][next2] == 'f' || a[next1][next2] == 'g' || a[next1][next2] == 'h') {
                    System.out.println("都是一个对的，为什么要互相残杀呢？请重新输入：");
                    jump ++;
                } else {
                    if (a[next1][next2] == '1') {
                        System.out.println("您不可以送死哦，请重新输入指令：");
                        jump ++;
                    } else {
                        a[next1][next2] = (char) (num + 48);
                        a[now1][now2] = '0';
                    }
                }
            }
        }
        return jump;
    }

    public static int moveoftigerandlone1(int jump, int now1, int now2, int next1, int next2, int next3, int next4, char num, char[][] a, char[][] b) {//用于左方狮虎的移动。
        if (b[next1][next2] == '2') {//在己方的陷阱中，无论是对方什么动物，都可以吃掉。
            a[next1][next2] = num;
            a[now1][now2] = '0';
        } else {
            if (b[next1][next2] == '1') {//需要判断虎或者狮子能不能过河。
                boolean judge = false;
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (b[i][j] == '1') {
                            if (a[i][j] != 'a') {//对方老鼠不在河里，可以过河。
                                if (a[next3][next4] != '1' || a[next3][next4] != '2' || a[next3][next4] != '3' || a[next3][next4] != '4' || a[next3][next4] != '5' || a[next3][next4] != '6' || a[next3][next4] != '7' || a[next3][next4] != '8') {
                                    if (a[next3][next4] != (char) (num + 49) || a[next3][next4] != (char) (num + 50))
                                        judge = true;
                                }
                            }
                        }
                    }
                }
                if (judge == true) {//能过河
                    a[next3][next4] = num;
                    a[now1][now2] = '0';
                } else {
                    System.out.println("你的狮虎不能过河哦，请重新输入：");
                    jump ++;
                }
            } else {
                if (a[next1][next2] == '1' || a[next1][next2] == '2' || a[next1][next2] == '3' || a[next1][next2] == '4' || a[next1][next2] == '5' || a[next1][next2] == '6' || a[next1][next2] == '7' || a[next1][next2] == '8') {
                    System.out.println("都是一个对的，为什么要互相残杀呢？请重新输入：");
                    jump ++;
                } else {
                    if (a[next1][next2] == (char) (num + 49) || a[next1][next2] == (char) (num + 50)) {
                        System.out.println("您不能送死哦，请重新输入：");
                        jump ++;
                    } else {
                        a[next1][next2] = num;
                        a[now1][now2] = '0';
                    }
                }
            }
        }
        return jump;
    }

    public static int moveoftigerandlone2(int jump, int now1, int now2, int next1, int next2, int next3, int next4, char num, char[][] a, char[][] b) {//用于右方狮虎的移动。
        if (b[next1][next2] == '4') {//在己方的陷阱中，无论是对方什么动物，都可以吃掉。
            a[next1][next2] = (char) (num + 48);
            a[now1][now2] = '0';
        } else {
            if (b[next1][next2] == '1') {//需要判断虎或者狮子能不能过河。
                boolean judge = false;
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (b[i][j] == '1') {
                            if (a[i][j] != '1') {//对方老鼠不在河里，可以过河。
                                if (a[next3][next4] != 'a' || a[next3][next4] != 'b' || a[next3][next4] != 'c' || a[next3][next4] != 'd' || a[next3][next4] != 'e' || a[next3][next4] != 'f' || a[next3][next4] != 'g' || a[next3][next4] != 'h') {
                                    if (a[next3][next4] <= num)
                                        judge = true;
                                }
                            }
                        }
                    }
                }
                if (judge == true) {//能过河
                    a[next3][next4] = (char) (num + 48);
                    a[now1][now2] = '0';
                } else {
                    System.out.println("你的狮虎不能过河哦，请重新输入：");
                    jump ++;
                }
            } else {
                if (a[next1][next2] == 'a' || a[next1][next2] == 'b' || a[next1][next2] == 'c' || a[next1][next2] == 'd' || a[next1][next2] == 'e' || a[next1][next2] == 'f' || a[next1][next2] == 'g' || a[next1][next2] == 'h') {
                    System.out.println("都是一个对的，为什么要互相残杀呢？请重新输入：");
                    jump ++;
                } else {
                    if (a[next1][next2] > num) {
                        System.out.println("您不能送死哦，请重新输入：");
                        jump ++;
                    } else {
                        a[next1][next2] = (char) (num + 48);
                        a[now1][now2] = '0';
                    }
                }
            }
        }
        return jump;
    }

    public static int moveofcommon1(int jump, int now1, int now2, int next1, int next2, char num, char[][] a, char[][] b) {//用于左方普通动物的移动。
        if (b[next1][next2] == '1') {
            System.out.println("您的兽不能下水哦，请重新输入：");
            jump ++;
        } else {
            if (b[next1][next2] == '2') {//在己方的陷阱中，无论是对方什么动物，都可以吃掉。
                a[next1][next2] = num;
                a[now1][now2] = '0';
            } else {
                if (a[next1][next2] == '1' || a[next1][next2] == '2' || a[next1][next2] == '3' || a[next1][next2] == '4' || a[next1][next2] == '5' || a[next1][next2] == '6' || a[next1][next2] == '7' || a[next1][next2] == '8') {
                    System.out.println("都是一个对的，为什么要互相残杀呢？请重新输入：");
                    jump ++;
                } else {
                    if (a[next1][next2] == (char) (num + 48) || a[next1][next2] == (char) (num + 49) || a[next1][next2] == (char) (num + 50) || a[next1][next2] == (char) (num + 51) || a[next1][next2] == (char) (num + 52) || a[next1][next2] == (char) (num + 53)) {
                        System.out.println("你不可以送死哦，请重新输入;");
                        jump ++;
                    } else {
                        a[next1][next2] = num;
                        a[now1][now2] = '0';
                    }
                }
            }
        }
        return jump;
    }

    public static int moveofcommon2(int jump, int now1, int now2, int next1, int next2, char num, char[][] a, char[][] b) {
        if (b[next1][next2] == '1') {
            System.out.println("您的兽不能下水哦，请重新输入：");
            jump ++;
        } else {
            if (b[next1][next2] == '4') {//在己方的陷阱中，无论是对方什么动物，都可以吃掉。
                a[next1][next2] = (char) (num + 48);
                a[now1][now2] = '0';
            } else {
                if (a[next1][next2] == 'a' || a[next1][next2] == 'b' || a[next1][next2] == 'c' || a[next1][next2] == 'd' || a[next1][next2] == 'e' || a[next1][next2] == 'f' || a[next1][next2] == 'g' || a[next1][next2] == 'h') {
                    System.out.println("都是一个对的，为什么要互相残杀呢？请重新输入：");
                    jump ++;
                } else {
                    if (a[next1][next2] > num) {
                        System.out.println("你不可以送死哦，请重新输入;");
                        jump ++;
                    } else {
                        a[next1][next2] = (char) (num + 48);
                        a[now1][now2] = '0';
                    }
                }
            }
        }
        return jump;
    }

    private static char[][] copyBoard(char[][] array) {
        char[][] newArray = new char[7][9];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                newArray[i][j] = array[i][j];
            }
        }
        return newArray;
    }
}

