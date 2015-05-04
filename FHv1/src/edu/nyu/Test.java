package edu.nyu;


import java.io.IOException;


// Class to test the parser

    public class Test {
        public static void main(String[] args) throws IOException {
            //Validate.isTrue(args.length == 1, "usage: supply url to fetch");
            PageParser pg=new PageParser();
            pg.url="http://www.food.com/recipe/asian-grilled-chicken-174899";
            pg.parsePage();
//            pg.url="http://www.saveur.com/article/Recipes/Rhubarb-Muffins-with-Almond-Streusel?dom=SAV&loc=recent&lnk=1&con=rhubarb-muffins-with-almond-streusel";
//            pg.parsePage();
//            System.out.println("**************************************************************************************");
            pg.url= "http://www.yummly.com/recipe/Garlic-penne-alfredo-with-sun-dried-tomatoes-299172?columns=4&position=8%2F82";
            pg.parsePage();
//            System.out.println("**************************************************************************************");
            pg.url="http://allrecipes.com/Recipe/Tangy-Grilled-Pork-Tenderloin/Detail.aspx?soid=carousel_0_rotd&prop24=rotd";
            pg.parsePage();
//            System.out.println("**************************************************************************************");
//            pg.url="http://www.tarladalal.com/5-Spice-Vegetable-Fried-Rice-8631r";
//            pg.parsePage();
//            System.out.println("**************************************************************************************");
//            pg.url="http://www.yummly.com/recipe/Savory-Spinach_-Feta_-and-Roasted-Red-Pepper-Muffins-Food_com-128632?columns=3&position=14%2F15";
//            pg.parsePage();
           //            pg.url="http://www.bonappetit.com/recipe/peach-blueberry-ice-cream-pie";
//            pg.parsePage();
//            System.out.println("**************************************************************************************");
//            pg.url="http://www.tarladalal.com/pav-bhaji-burger-video-by-tarla-dalal-852v";
//            pg.parsePage();
//            System.out.println("**************************************************************************************");
        }



    }


