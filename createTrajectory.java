import java.util.ArrayList;

public class createTrajectory {

    public static void main(String[] args){

        rawTrajectory refTraj_utm =  new rawTrajectory("D:\\kntu\\payanname\\data\\geolife\\synthetic\\referenceTrajectory_UTMzone50.xlsx", "X", "Y");


        Double[] rate = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        Double[] distance = {10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0};

        String path = "D:\\kntu\\payanname\\data\\geolife\\synthetic\\Tr_";
        for(int k=0; k<rate.length; k++) {
                for (int i = 0; i < 100; i++) {
                    rawTrajectory transformedTrajectory = refTraj_utm.increasing(rate[k]);
                    transformedTrajectory.saveTrajecory(path + "increase_" + Integer.toString((int)(rate[k]*100)) + "_" + Integer.toString(i) + ".xlsx");
                }
        }

        for(int k=0; k<rate.length; k++) {
            for (int i = 0; i < 100; i++) {
                rawTrajectory transformedTrajectory = refTraj_utm.decreasing(rate[k]);
                transformedTrajectory.saveTrajecory(path + "decrease_" + Integer.toString((int)(rate[k]*100)) + "_" + Integer.toString(i) + ".xlsx");
            }
        }

        for(int k=0; k<rate.length; k++) {
            for (int j = 0; j < distance.length; j++) {
                for (int i = 0; i < 100; i++) {
                    rawTrajectory transformedTrajectory = refTraj_utm.randomShift(rate[k], distance[j]);
                    transformedTrajectory.saveTrajecory(path + "shift_" + Integer.toString((int)(rate[k]*100)) + "_" + Integer.toString((int)(distance[j] + 0)) + "_" + Integer.toString(i) + ".xlsx");
                }
            }
        }

        for(int k=0; k<rate.length; k++) {
            for (int j = 0; j < distance.length; j++) {
                for (int i = 0; i < 100; i++) {
                    rawTrajectory transformedTrajectory = refTraj_utm.addNoise(rate[k], distance[j]);
                    transformedTrajectory.saveTrajecory(path + "noise_" + Integer.toString((int)(rate[k]*100)) + "_" + Integer.toString((int)(distance[j] + 0)) + "_" + Integer.toString(i) + ".xlsx");
                }
            }
        }



    }


}
