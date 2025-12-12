/* go-track trajectories */

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class semanticTrajectory extends rawTrajectory {

    ArrayList<Double> speed = new ArrayList<>();
    double meanSpeed;           // Km/H
    double time;                // total time (Hour)
    double distance;            // Km
    int ratingTraffic;          // traffic rating: good-3 / normal-2 / bad-1
    int ratingBus;              // number of people in the bus: crowded-3 / not crowded-2 / empty-1
    int ratingWeather;          // raining-1 / sunny-2
    int carOrBus;               // car-1 / bus-2
    int trackId;                // trackId

    semanticTrajectory (int trackId, ArrayList<Double> latitude, ArrayList<Double> longitude){

        super(latitude,longitude);
        this.trackId = trackId;

    }

    semanticTrajectory (int trackId, ArrayList<Double> latitude, ArrayList<Double> longitude, double meanSpeed, ArrayList<Double> speed, double time, double distance, int ratingTraffic, int ratingBus, int ratingWeather, int carOrBus){

        super(latitude,longitude);
        this.trackId = trackId;
        this.meanSpeed = meanSpeed;
        this.speed = speed;
        this.time = time;
        this.distance = distance;
        this.ratingTraffic = ratingTraffic;
        this.ratingWeather = ratingWeather;
        this.ratingBus = ratingBus;
        this.carOrBus = carOrBus;

    }

    void setMeanSpeed(double meanSpeed){

        this.meanSpeed = meanSpeed;

    }

    void setSpeed (ArrayList<Double> speed){

        this.speed = speed;

    }

    void setTime (double time){

        this.time = time;

    }

    void setDistance (double distance){

        this.distance = distance;

    }

    void setRatingTraffic (int ratingTraffic){

        this.ratingTraffic = ratingTraffic;

    }

    void setRatingBus (int ratingBus){

        this.ratingBus = ratingBus;

    }

    void setRatingWeather (int ratingWeather){

        this.ratingWeather = ratingWeather;

    }

    void setCarOrBus (int carOrBus){

        this.carOrBus = carOrBus;

    }

    static ArrayList<semanticTrajectory> createSemanticTrajectories(String tracksPath, String tracksPointPath) {
        ArrayList<semanticTrajectory> mySemanticTrajectories = new ArrayList<>();
        ArrayList<Double> lat = new ArrayList<>();
        ArrayList<Double> lng = new ArrayList<>();
        int trackId = 1;
        try {
            FileInputStream pointsFile = new FileInputStream(new File(tracksPointPath));
            Workbook pointsWorkbook = new XSSFWorkbook(pointsFile);
            Sheet pointsSheet = pointsWorkbook.getSheetAt(0);
            Iterator<Row> pointsRowIterator = pointsSheet.iterator();
            Row headerRow = pointsRowIterator.next();

            while (pointsRowIterator.hasNext()) {
                Row pointsCurrentRow = pointsRowIterator.next();
                Iterator<Cell> pointsCellIterator = pointsCurrentRow.iterator();

                for (int iter = 0; iter < 3; iter++) {
                    Cell pointsCurrentCell = pointsCellIterator.next();
                    if (iter == 0 & (int) pointsCurrentCell.getNumericCellValue() != trackId) {
                        ArrayList<Double> newLat = new ArrayList<>(lat);
                        ArrayList<Double> newLng = new ArrayList<>(lng);
                        mySemanticTrajectories.add(new semanticTrajectory(trackId, newLat, newLng));
                        lat.clear();
                        lng.clear();
                        trackId = (int) pointsCurrentCell.getNumericCellValue();
                    }
                    if (iter == 1) lat.add(pointsCurrentCell.getNumericCellValue());
                    if (iter == 2) lng.add(pointsCurrentCell.getNumericCellValue());
                }
            }
            mySemanticTrajectories.add(new semanticTrajectory(trackId, lat, lng));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            FileInputStream tracksFile = new FileInputStream(new File(tracksPath));
            Workbook tracksWorkbook = new XSSFWorkbook(tracksFile);
            Sheet tracksSheet = tracksWorkbook.getSheetAt(0);
            Iterator<Row> tracksRowIterator = tracksSheet.iterator();
            Row headerRow = tracksRowIterator.next();
            int i = 0;
            while (tracksRowIterator.hasNext()) {

                Row tracksCurrentRow = tracksRowIterator.next();
                Iterator<Cell> tracksCellIterator = tracksCurrentRow.iterator();

                Cell trackIdCell = tracksCellIterator.next();
                if ((int) trackIdCell.getNumericCellValue() == mySemanticTrajectories.get(i).trackId){
                    for (int iter = 0; iter < 7; iter++) {
                        Cell trackCurrentCell = tracksCellIterator.next();
                        switch (iter){
                            case 0:
                                mySemanticTrajectories.get(i).setMeanSpeed(trackCurrentCell.getNumericCellValue());
                                break;
                            case 1:
                                mySemanticTrajectories.get(i).setTime(trackCurrentCell.getNumericCellValue());
                                break;
                            case 2:
                                mySemanticTrajectories.get(i).setDistance(trackCurrentCell.getNumericCellValue());
                                break;
                            case 3:
                                mySemanticTrajectories.get(i).setRatingTraffic((int)trackCurrentCell.getNumericCellValue());
                                break;
                            case 4:
                                mySemanticTrajectories.get(i).setRatingBus((int)trackCurrentCell.getNumericCellValue());
                                break;
                            case 5:
                                mySemanticTrajectories.get(i).setRatingWeather((int)trackCurrentCell.getNumericCellValue());
                                break;
                            case 6:
                                mySemanticTrajectories.get(i).setCarOrBus((int)trackCurrentCell.getNumericCellValue());
                                break;
                        }
                    }
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        double timeSpan;
        for (int i = 0; i < mySemanticTrajectories.size(); i++){
            ArrayList<Double> speed = new ArrayList<>();

            if (mySemanticTrajectories.get(i).getLength() > 1){

                timeSpan = mySemanticTrajectories.get(i).time / (mySemanticTrajectories.get(i).getLength() - 1);
                speed.add(0.0);

                for (int j=0; j<mySemanticTrajectories.get(i).getLength()-1; j++){

                    double distance = Math.sqrt(Math.pow(mySemanticTrajectories.get(i).getLatitude().get(j+1)-mySemanticTrajectories.get(i).getLatitude().get(j),2)+Math.pow(mySemanticTrajectories.get(i).getLongitude().get(j+1)-mySemanticTrajectories.get(i).getLongitude().get(j),2));
                    speed.add(distance/timeSpan);

                }
            } else {

                speed.add(mySemanticTrajectories.get(i).distance/mySemanticTrajectories.get(i).time);

            }
            mySemanticTrajectories.get(i).setSpeed(speed);
        }
        return mySemanticTrajectories;
    }

    public static void main(String[] args) {

        String tracksPointPath = "D:\\kntu\\art2\\trajectories\\trackspoints_utm.xlsx";
        String tracksPath = "D:\\kntu\\art2\\trajectories\\tracks.xlsx";
        ArrayList<semanticTrajectory> totalTrajectories = createSemanticTrajectories(tracksPath, tracksPointPath);
        ArrayList<semanticTrajectory> myTrajectories = new ArrayList<>();

        for (semanticTrajectory trajectory: totalTrajectories) {
            if (trajectory.getLength()>30){

                myTrajectories.add(trajectory);

            }
        }





    }
}
