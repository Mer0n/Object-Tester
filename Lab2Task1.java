package MeresaMeron_Lab2;

public class Lab2Task1 {

    //method that takes an array of FuelConsumable objects and print each object

    //that needs electricity to work  ex. Polymorphism

    static void printElectronicItems(FuelUsed objects[]) {
            for (FuelUsed ob : objects) {
        
            if (ob.needsElectricity()) {         

                System.out.println(ob);

            }

        }

    }

    public static void main(String[] args) {

        //creating an array of 5 FuelConsumable objects

        FuelUsed array[] = new FuelUsed[5];

       
        //non electric bicycle
        array[0] = new Bicycle(6, true, false); 
        //electric bicycle
        array[1] = new Bicycle(8, true, true); 
        //Freezer need electricity
        array[2] = new Freezer(Freezer.Size.CHEST, true, true);
        // Horse does not need electricity
        array[3] = new Horse("Stallion");
        // TV needs electricity
        array[4] = new TV(43, true, "UHD");

        //printing all objects

        System.out.println("All objects: ");

        for (FuelUsed ob : array) {

            System.out.println(ob);

        }

        //printing only the electronic items

        System.out.println("\nAll objects that needs electricty: ");

        printElectronicItems(array);

    }

}
