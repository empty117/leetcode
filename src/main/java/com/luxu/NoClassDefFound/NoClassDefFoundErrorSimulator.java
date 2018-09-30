package com.luxu.NoClassDefFound;

public class NoClassDefFoundErrorSimulator {
       
        /**
         * @param args
         */
        public static void main(String[] args) {
//               System.out.println("java.lang.NoClassDefFoundError Simulator - Training 2");
//               System.out.println("Author: Pierre-Hugues Charbonneau");
//               System.out.println("http://javaeesupportpatterns.blogspot.com\n\n");
                             
               try {                 
                       // Create a new instance of ClassA (attempt #1)
                       System.out.println("FIRST attempt to create a new instance of ClassA...\n");        
                       ClassA classA = new ClassA();
                      
               } catch (Throwable any) {
                       any.printStackTrace();
               }             
              
               try {
                       // Create a new instance of ClassA (attempt #2)
                       System.out.println("\nSECOND attempt to create a new instance of ClassA...\n");
                       ClassA classA = new ClassA();

               } catch (Throwable any) {
                       any.printStackTrace();
               }
//
//               try {
//                       // Create a new instance of ClassA (attempt #3)
//                       System.out.println("\nTHIRD attempt to create a new instance of ClassA...\n");
//                       ClassA classA = new ClassA();
//
//               } catch (Throwable any) {
//                       any.printStackTrace();
//               }
              
               System.out.println("\n\ndone!");
        }
}