import nl.saxion.app.CsvReader;
import nl.saxion.app.SaxionApp;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Application implements Runnable {
    public static void main(String[] args) {
        SaxionApp.start(new Application(), 1440, 900);
    }

    boolean doRun = true;
    String itemFamily;
    String itemType;
    String itemName;
    String itemBrand;
    String isGearSetPiece;
    String filePath = "DivisionInventory.csv";

    Color Legendary = SaxionApp.createColor(255, 175, 16);
    Color GSP = SaxionApp.createColor(23, 200, 132);

    ArrayList<Item> allItems = new ArrayList<>();
    String[] weaponTypes = {"Rifle", "Assault", "Marksman", "SMG", "LMG", "Shotgun", "Sidearm"};
    String[] gearTypes = {"Helmet", "Bag", "Chestpiece", "Gloves", "Holster", "Kneepads"};
    Brand[] brands = new Brand[23];
    GearSet[] gearSets = new GearSet[13];
    String[] allGear = {
            "5.11 Tactical",
            "Airaldi Holdings",
            "Alps Summit Armament",
            "Badger Tuff",
            "Ceska Vyroba s.r.o.",
            "China Light Industries Corporation",
            "Douglas & Harding",
            "Fenris Group AB",
            "Gila Guard",
            "Grupo Sombra S.A.",
            "Golan Gear",
            "Belstone Armory",
            "Hana-U Corporation",
            "Murakami Industries",
            "Overlord Armaments",
            "Petrov Defense Group",
            "Providence Defense",
            "Richter & Kaiser GmbH",
            "Sokolov Concern",
            "Walker, Harris & Co.",
            "Wyvern Wear",
            "Empress International",
            "Yaahl Gear",
            "Ongoing Directive",
            "Hard Wired",
            "True Patriot",
            "Aces and Eights",
            "Tip of the Spear",
            "Negotiator's Dilemma",
            "Striker's Battlegear",
            "System Corruption",
            "Future Initiative",
            "Foundry Bulwark",
            "Eclipse Protocol",
            "Hunter's Fury",
            "Rigger",
    };

    boolean runApplication = true;

    public void fillBrandsArray() {
        CsvReader brandReader = new CsvReader("BrandSets.csv");
        brandReader.skipRow();
        int index = 0;
        while (brandReader.loadRow()) {
            Brand brand = new Brand(brandReader.getString(0), brandReader.getString(1), brandReader.getString(2),
                    brandReader.getString(3), brandReader.getString(4), brandReader.getString(5));
            brands[index] = brand;
            index++;
        }
    }
    public void fillGearSetsArray() {
        CsvReader gearSetReader = new CsvReader("GearSets.csv");
        gearSetReader.skipRow();
        int index = 0;
        while (gearSetReader.loadRow()) {
            GearSet gearSet = new GearSet(gearSetReader.getString(0),gearSetReader.getString(1), gearSetReader.getString(2),
                    gearSetReader.getString(3),gearSetReader.getString(4),gearSetReader.getString(5),gearSetReader.getString(6));
            gearSets[index] = gearSet;
            index++;
        }
    }

    public void run() {
        startApplication();
    }

    private void startApplication() {
        fillBrandsArray();
        fillGearSetsArray();
        readCSV();
        showMenu();
    }

    private void showMenu() {
        SaxionApp.clear();
        SaxionApp.printLine("Division 2 Inventory Tracker");
        SaxionApp.printLine("----------------------------");
        SaxionApp.printLine("1. Add new item to inventory");
        SaxionApp.printLine("2. Delete item from inventory");
        SaxionApp.printLine("3. Show inventory");
        SaxionApp.printLine("0. Exit");

        menuLogic();
    }

    private void menuLogic() {
        while (runApplication) {
            int userChoice = SaxionApp.readInt();
            if (userChoice == 1) {
                saveRecord(filePath);
            } else if (userChoice == 2) {
                deleteRecord();
            } else if (userChoice == 3) {
                showInventory();
            } else if (userChoice == 0) {
                runApplication = false;
            } else {
                SaxionApp.printLine("Choose a valid option!", Color.red);
                SaxionApp.pause();
                SaxionApp.removeLastPrint();
                SaxionApp.removeLastPrint();
            }
        }
    }


    public void saveRecord(String filePath) {
        SaxionApp.printLine("Is the item a weapon or gearpiece (w/g)");
        askWeaponOrGear();
        SaxionApp.printLine(itemFamily, Color.green);
        if (itemFamily.equals("Weapon")) {
            askWeaponInfo();
        } else {
            askGearInfo();
            askGearBrand();
        }
        askItemName();


        try{
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(itemFamily+";"+itemType+";"+itemName+";"+itemBrand+";"+isGearSetPiece);
            pw.flush();
            pw.close();
            JOptionPane.showMessageDialog(null,"Record added");
        } catch (Exception E) {
            JOptionPane.showMessageDialog(null,"Record adding failed");
        }
        showMenu();
    }

    private void askWeaponOrGear() {
        boolean incorrectInput = true;
        while (incorrectInput) {
            char itemFamilyInput = SaxionApp.readChar();
            if (itemFamilyInput=='w' || itemFamilyInput=='W') {
                incorrectInput = false;
                itemFamily = "Weapon";
            } else if (itemFamilyInput=='g' || itemFamilyInput=='G') {
                incorrectInput = false;
                itemFamily = "Gear";
            } else {
                SaxionApp.printLine("Press either the 'w' or 'g' key as input", Color.red);
                SaxionApp.pause();
                SaxionApp.removeLastPrint();
            }
        }
    }

    private void askWeaponInfo() {
        SaxionApp.printLine("What is the weapon type? (Rifle, Assault, Marksman, SMG, LMG, Shotgun, Sidearm)");
        boolean incorrectInput = true;
        while (incorrectInput) {
            itemType = SaxionApp.readString();

            for (String weaponType : weaponTypes) {
                if (itemType.equals(weaponType)) {
                    SaxionApp.removeLastPrint();
                    incorrectInput = false;
                    break;
                }
            }

            if (incorrectInput) {
                SaxionApp.printLine("Enter a valid weapon type", Color.red);
                SaxionApp.pause();
                SaxionApp.removeLastPrint();
                SaxionApp.removeLastPrint();
            }
        }
        SaxionApp.printLine(itemType, Color.green);
        SaxionApp.pause();
    }

    private void askGearInfo() {
        SaxionApp.printLine("What is the gear type? (Helmet, Bag, Chestpiece, Gloves, Holster, Kneepads)");
        boolean incorrectInput = true;
        while (incorrectInput) {
            itemType = SaxionApp.readString();

            for (String gearType : gearTypes) {
                if (itemType.equals(gearType)) {
                    SaxionApp.removeLastPrint();
                    incorrectInput = false;
                    break;
                }
            }
            if (incorrectInput) {
                SaxionApp.printLine("Enter a valid gear type", Color.red);
                SaxionApp.pause();
                SaxionApp.removeLastPrint();
                SaxionApp.removeLastPrint();
            }
        }
        SaxionApp.printLine(itemType, Color.green);
    }

    private void askItemName() {
        SaxionApp.printLine("What is the name of the item?");
        boolean incorrectInput = true;
        while (incorrectInput) {
            itemName = SaxionApp.readString();
            SaxionApp.removeLastPrint();
            SaxionApp.printLine(itemName, Color.orange);
            SaxionApp.printLine("Is this correctly spelled? (y/n)");
            char correctSpelling = SaxionApp.readChar();
            if (correctSpelling == 'y') {
                incorrectInput = false;
                SaxionApp.removeLastPrint();
                SaxionApp.removeLastPrint();
                SaxionApp.printLine(itemName, Color.green);
            } else {
                SaxionApp.removeLastPrint();
                SaxionApp.removeLastPrint();
            }
        }
    }

    private void askGearBrand() {
        SaxionApp.printLine("What is the brand of the item? (1-36)");

        SaxionApp.turnBorderOff();
        SaxionApp.setFill(Legendary);

        char pageControl;
        int page = 1;
        boolean checkBrands = true;
        SaxionApp.drawBorderedText("BRANDS", 950, 20, 20);
        SaxionApp.drawImage("media/images/BrandsPage1.png", 800, 40, 469, 892);
        SaxionApp.drawBorderedText("Page 1", 960, 870, 20);
        int pagination = 1;
        while (checkBrands) {
            pageControl = SaxionApp.readChar();
            if (pageControl == 'd' && pagination != 3) {
                pagination++;
            } else if (pageControl == 'a' && pagination != 1) {
                pagination--;
            } else if (pageControl == 'q') {
                checkBrands = false;
            }

            if (pagination == 1) {
                SaxionApp.setFill(Legendary);
                SaxionApp.removeLastDraw();
                SaxionApp.removeLastDraw();
                SaxionApp.removeLastDraw();
                SaxionApp.drawBorderedText("BRANDS", 950, 20, 20);
                SaxionApp.drawImage("media/images/BrandsPage1.png", 800, 40, 469, 892);
                SaxionApp.drawBorderedText("Page 1", 960, 870, 20);
            } else if (pagination == 2) {
                SaxionApp.setFill(Legendary);
                SaxionApp.removeLastDraw();
                SaxionApp.removeLastDraw();
                SaxionApp.removeLastDraw();
                SaxionApp.drawBorderedText("BRANDS", 950, 20, 20);
                SaxionApp.drawImage("media/images/BrandsPage2.png", 800, 40, 469, 892);
                SaxionApp.drawBorderedText("Page 2", 960, 870, 20);
            } else if (pagination == 3) {
                SaxionApp.setFill(GSP);
                SaxionApp.removeLastDraw();
                SaxionApp.removeLastDraw();
                SaxionApp.removeLastDraw();
                SaxionApp.drawBorderedText("GEAR SET BRANDS", 900, 20, 20);
                SaxionApp.drawImage("media/images/BrandsPage3.png", 800, 40, 469, 892);
                SaxionApp.drawBorderedText("Page 3", 960, 870, 20);
            }  else if (pageControl == 'q') {
                checkBrands = false;
            }

        }


//        int index = 1;
//        int brandX = 700;
//        int brandY = 50;
//
//        for (int i = 0; i < gearBrands.length; i++) {
//            SaxionApp.drawBorderedText(i+1 + ". " + gearBrands[i], 700, brandY, 25);
//            brandY+=25;
//
//            if (i == (gearBrands.length/2+1)) {
//                pageControl = SaxionApp.readChar();
//                if (pageControl == 'd') {
//                    for (int j = 0; j < 20; j++) {
//                        SaxionApp.removeLastDraw();
//                    }
//                    brandY = 50;
//                } else if (pageControl == 'a') {
//                    SaxionApp.setFill(Legendary);
//                    i = 0;
//                    brandY = 50;
//                } else if (pageControl == 'q') {
//                    break;
//                }
//            }
//            if (i >= 22) {
//                SaxionApp.setFill(GSP);
//            }
//        }
//
//        for (String gearBrand : gearBrands) {
//
//
//            SaxionApp.drawBorderedText(index + ". " + gearBrand, brandX, brandY, 25);
//            index++;
//            brandY+=25;
//            if (index == (gearBrands.length/2+1)) {
//                brandX += 425;
//                brandY = 50;
//            }
//            if (index >= 24) {
//                SaxionApp.setFill(GSP);
//            }
//        }


        // Choose which brand it is
        int index = 1;
        int brandChoice = 0;
        boolean incorrectInput = true;
        while (incorrectInput) {
            index = 1;
            brandChoice = SaxionApp.readInt();
            if (brandChoice < 1 || brandChoice > allGear.length) {
                SaxionApp.printLine("That is not a choice, try again!", Color.red);
                SaxionApp.pause();
                SaxionApp.removeLastPrint();
                SaxionApp.removeLastPrint();
            } else {
                SaxionApp.removeLastPrint();
                SaxionApp.printLine(allGear[brandChoice-1], Color.orange);

                if (brandChoice < 24) {
                    for (Brand brand : brands) {
                        System.out.println(allGear[brandChoice-1]);
                        System.out.println(brand.brandSetName);
                        if (allGear[brandChoice-1].equals(brand.brandSetName)) {
                            SaxionApp.drawImage("media/images/brands/"+brand.icon+".png", 350, 200, 125, 125);
                        }
                    }
                } else {
                    for (GearSet gearSet : gearSets) {
                        if (allGear[brandChoice-1].equals(gearSet.gearSetName)) {
                            SaxionApp.drawImage("media/images/brands/"+gearSet.icon+".png", 350, 200, 125, 125);
                        }
                    }
                }

                SaxionApp.printLine("Is this the right brand? (y/n)");
                char correctChoice = SaxionApp.readChar();
                if (correctChoice == 'y') {
                    for (String gearBrand : allGear) {
                        if (index == brandChoice) {
                            itemBrand = gearBrand;
                            SaxionApp.removeLastPrint();
                            SaxionApp.removeLastPrint();
                            SaxionApp.removeLastDraw();
                            SaxionApp.printLine(allGear[brandChoice-1], Color.green);
                            incorrectInput = false;
                            break;
                        } else {
                            index++;
                        }
                    }
                } else if (correctChoice == 'n') {
                    SaxionApp.removeLastDraw();
                    SaxionApp.removeLastPrint();
                    SaxionApp.removeLastPrint();
                    SaxionApp.printLine("Enter a different choice!", Color.cyan);
                    SaxionApp.pause();
                    SaxionApp.removeLastPrint();
                }
            }
        }

        SaxionApp.print("Is it a ");
        SaxionApp.print("Gear Set Piece", GSP);
        SaxionApp.printLine("? [AUTOMATED]");
        if (brandChoice >= 24) {
            SaxionApp.printLine("Yes", GSP);
            isGearSetPiece = "y";
        } else {
            SaxionApp.printLine("No", Legendary);
            isGearSetPiece = "n";
        }
    }

    public void readCSV () {
        CsvReader reader = new CsvReader("DivisionInventory.csv");
        reader.skipRow();
        while (reader.loadRow()) {
            Item item = new Item(reader.getString(0), reader.getString(1), reader.getString(2), reader.getString(3), reader.getString(4));
            allItems.add(item);
        }
    }

    public void readRecord () {
        try {
            for (Item item : allItems) {
                System.out.println(item.itemName);

            }
        }
        catch (Exception E) {

        }
    }

    public void deleteRecord () {

        SaxionApp.printLine("Which do you want removed?");
        int removeRow = 0;
        boolean invalidInput = true;
        while (invalidInput) {
            removeRow = SaxionApp.readInt()-1;
            if (removeRow >= 0) {
                invalidInput = false;
            } else {
                SaxionApp.printLine("Choose a valid option", Color.red);
                SaxionApp.pause();
                SaxionApp.removeLastPrint();
                SaxionApp.removeLastPrint();
            }
        }

        allItems.remove(removeRow);
        ArrayList<Item> tempItemList = new ArrayList<>(allItems);
        try{
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println("Item Family;Item Type;Item Name;Item Brand;isGearSetPiece");

            for (Item item : tempItemList) {
                pw.println(item.itemFamily+";"+item.itemType+";"+item.itemName+";"+item.itemBrand+";"+item.isGearSetPiece);
            }

            pw.flush();
            pw.close();

            JOptionPane.showMessageDialog(null,"Record deleted");
        } catch (Exception E) {
            JOptionPane.showMessageDialog(null,"Record deletion failed");
        }
    }


    private void showInventory() {
        SaxionApp.turnBorderOff();
        SaxionApp.setFill(Color.white);
        SaxionApp.drawBorderedText("Division 2 Inventory", 0, 0, 40);
        SaxionApp.drawBorderedText("Return to main menu with by pressing 'Q'", 0, 45, 25);

        SaxionApp.turnBorderOn();
        SaxionApp.setBorderColor(Color.black);

        int itemY = 90;
        int itemX = 5;

        for (Item item : allItems) {
            SaxionApp.setFill(Legendary);
            SaxionApp.drawRectangle(itemX, itemY, 200, 100);
            SaxionApp.setFill(Color.white);
            SaxionApp.drawBorderedText(item.itemName, itemX+5, itemY+5, 20);
            itemX+=200;
        }

    }

    // Easy Access Checks


}