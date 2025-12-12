import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.la4j.Matrix;
import org.la4j.inversion.GaussJordanInverter;
import org.la4j.matrix.dense.Basic2DMatrix;

public class rawTrajectory {

    private ArrayList<Double> latitude;
    private ArrayList<Double> longitude;

    rawTrajectory(){}

    rawTrajectory(ArrayList<Double> latitude, ArrayList<Double> longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    rawTrajectory(String FileName, String latitudeHeader, String longitudeHeader) {
        ArrayList<Double> lat = new ArrayList<>();
        ArrayList<Double> lng = new ArrayList<>();
        try {
            FileInputStream excelFile = new FileInputStream(new File(FileName));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = datatypeSheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row headerRow = datatypeSheet.getRow(0);
                Iterator<Cell> headerCellIterator = headerRow.iterator();
                Row currentRow = rowIterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                while (cellIterator.hasNext()) {
                    String Header = headerCellIterator.next().getStringCellValue();
                    Cell currentCell = cellIterator.next();
                    if (Header.compareTo(latitudeHeader) == 0) {
                        lat.add(currentCell.getNumericCellValue());
                    } else if (Header.compareTo(longitudeHeader) == 0) {
                        lng.add(currentCell.getNumericCellValue());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.longitude = lng;
        this.latitude = lat;
    }

    static rawTrajectory createRawTrajectoryFromExcel(String path){
        ArrayList<Double> lat = new ArrayList<>();
        ArrayList<Double> lng = new ArrayList<>();

        Deg2UTM converter = new Deg2UTM();

        try{

            FileInputStream excelFile = new FileInputStream(new File(path));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();


            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();



                Cell currentCell = cellIterator.next();
                Double latitude = currentCell.getNumericCellValue();

                currentCell = cellIterator.next();
                Double longitude = currentCell.getNumericCellValue();

                Double[] utm = converter.Deg2UTM(latitude, longitude);
                lat.add(utm[0]);
                lng.add(utm[1]);
            }


        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return new rawTrajectory(lat, lng);
    }

    int getLength() {
        return (latitude.size());
    }

    rawTrajectory Head() {
        ArrayList<Double> newLat = new ArrayList<>(latitude);
        ArrayList<Double> newLng = new ArrayList<>(longitude);
        newLat.remove(getLength() - 1);
        newLng.remove(getLength() - 1);
        return new rawTrajectory(newLat, newLng);
    }

    rawTrajectory Rest() {
        ArrayList<Double> newLat = new ArrayList<>(latitude);
        ArrayList<Double> newLng = new ArrayList<>(longitude);
        newLat.remove(0);
        newLng.remove(0);
        return new rawTrajectory(newLat, newLng);
    }

    void setLatitude(ArrayList<Double> latitude) {
        this.latitude = latitude;
    }

    ArrayList<Double> getLatitude() {
        return (latitude);
    }

    void setLongitude(ArrayList<Double> longitude) {
        this.longitude = longitude;
    }

    ArrayList<Double> getLongitude() {
        return (longitude);
    }

    double[] getElement(int index) {
        return new double[]{this.latitude.get(index), this.longitude.get(index)};
    }

    rawTrajectory normTrajectory() {
        ArrayList<Double> newLat = new ArrayList<>();
        ArrayList<Double> newLng = new ArrayList<>();
        double sumX = 0;
        double sumY = 0;
        double standardDeviationY = 0;
        double standardDeviationX = 0;
        for (double num : this.latitude) {
            sumY += num;
        }
        for (double num : this.longitude) {
            sumX += num;
        }
        double meanX = sumX / this.getLength();
        double meanY = sumY / this.getLength();
        for (double num : this.latitude) {
            standardDeviationY += Math.pow(num - meanY, 2);
        }
        for (double num : this.longitude) {
            standardDeviationX += Math.pow(num - meanX, 2);
        }
        standardDeviationX = Math.sqrt(standardDeviationX / this.getLength()-1);
        standardDeviationY = Math.sqrt(standardDeviationY / this.getLength()-1);
        for (double num : this.longitude) {
            newLng.add((num - meanX) / standardDeviationX);
        }
        for (double num : this.latitude) {
            newLat.add((num - meanY) / standardDeviationY);
        }
        return new rawTrajectory(newLat, newLng);
    }

    public static ArrayList<Double> normalize(ArrayList<Double> A){
        ArrayList<Double> normalA = new ArrayList<>();
        double sum = 0;
        double standardDeviation = 0;
        for (double num : A) {
            sum += num;
        }
        double mean = sum / A.size();
        for (double num : A) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        standardDeviation = Math.sqrt(standardDeviation / (A.size()-1));
        for (double num : A) {
            normalA.add((num - mean) / standardDeviation);
        }
        return normalA;
    }

    rawTrajectory addNoise(double rate, double distance) {
        double num = this.getLength() + Math.ceil(rate * this.getLength());
        ArrayList<Double> newLat = new ArrayList<>(this.latitude);
        ArrayList<Double> newLng = new ArrayList<>(this.longitude);
        while (newLat.size() < num) {
            int index = (int) (Math.random() * newLat.size());
            double sign1 = Math.round(Math.random());
            double sign2 = Math.round(Math.random());
            double xShift = Math.random() * distance;
            double yShift = Math.sqrt(Math.pow(distance, 2) - Math.pow(xShift, 2));
            if (sign1 == 0) newLat.add(index, newLat.get(index) + xShift);
            else newLat.add(index, newLat.get(index) - xShift);
            if (sign2 == 0) newLng.add(index, newLng.get(index) + yShift);
            else newLng.add(index, newLng.get(index) - yShift);
        }
        return new rawTrajectory(newLat, newLng);
    }

    rawTrajectory increasing(double rate) {
        double num = this.getLength() + Math.ceil(rate * this.getLength());
        ArrayList<Double> newLat = new ArrayList<>(latitude);
        ArrayList<Double> newLng = new ArrayList<>(longitude);
        while (newLat.size() < num) {
            int index = (int) (Math.random() * newLat.size());
            if (index == newLat.size() - 1) continue;
            newLat.add(index + 1, (newLat.get(index) + newLat.get(index + 1)) / 2);
            newLng.add(index + 1, (newLng.get(index) + newLng.get(index + 1)) / 2);
        }
        return new rawTrajectory(newLat, newLng);
    }

    rawTrajectory decreasing(double rate) {
        double num = Math.ceil(rate * this.getLength());
        ArrayList<Double> newLat = new ArrayList<>(latitude);
        ArrayList<Double> newLng = new ArrayList<>(longitude);
        for (int i = 0; i < num; i++) {
            int index = (int) (Math.random() * newLat.size());
            newLat.remove(index);
            newLng.remove(index);
        }
        return new rawTrajectory(newLat, newLng);
    }

    rawTrajectory decreasing(int a) {

        ArrayList<Double> newLat = new ArrayList<>();
        ArrayList<Double> newLng = new ArrayList<>();
        for (int i = 0; i < this.getLength()/a; i++) {
            int index = (int) (i*a);
            newLat.add(this.getLatitude().get(index));
            newLng.add(this.getLongitude().get(index));
        }
        return new rawTrajectory(newLat, newLng);
    }

    rawTrajectory randomShift(double rate, double distance) {
        double num = Math.ceil(rate * this.getLength());
        ArrayList<Double> newLat = new ArrayList<>(this.latitude);
        ArrayList<Double> newLng = new ArrayList<>(this.longitude);
        ArrayList<Integer> mainIndex = new ArrayList<>();
        for (int i = 0; i < this.getLength(); i++) {
            mainIndex.add(i);
        }
        for (int i = 0; i < num; i++) {
            int index = (int) Math.round(Math.random() * (mainIndex.size() - 1));
            double sign1 = Math.round(Math.random());
            double sign2 = Math.round(Math.random());
            double xShift = Math.random() * distance;
            double yShift = Math.sqrt(Math.pow(distance, 2) - Math.pow(xShift, 2));
            if (sign1 == 0)
                newLat.set(mainIndex.get(index), newLat.get(mainIndex.get(index)) + yShift);
            else
                newLat.set(mainIndex.get(index), newLat.get(mainIndex.get(index)) - yShift);
            if (sign2 == 0)
                newLng.set(mainIndex.get(index), newLng.get(mainIndex.get(index)) + xShift);
            else
                newLng.set(mainIndex.get(index), newLng.get(mainIndex.get(index)) - xShift);
            mainIndex.remove(index);
        }
        return new rawTrajectory(newLat, newLng);
    }

    rawTrajectory synchronizedShift(double rate, double minDistance, double maxDistance) {
        double sign1 = Math.round(Math.random());
        double sign2 = Math.round(Math.random());
        double distance = Math.random() * Math.abs(maxDistance - minDistance) + minDistance;
        double xShift = Math.random() * distance;
        double yShift = Math.sqrt(Math.pow(distance, 2) - Math.pow(xShift, 2));
        double num = Math.ceil(rate * this.getLength());
        ArrayList<Double> newLat = new ArrayList<>(this.latitude);
        ArrayList<Double> newLng = new ArrayList<>(this.longitude);
        ArrayList<Integer> mainIndex = new ArrayList<>();
        for (int i = 0; i < this.getLength(); i++) {
            mainIndex.add(i);
        }
        for (int i = 0; i < num; i++) {
            int index = (int) Math.round(Math.random() * (mainIndex.size() - 1));
            if (sign1 == 0) newLat.set(mainIndex.get(index), newLat.get(mainIndex.get(index)) + yShift);
            else newLat.set(mainIndex.get(index), newLat.get(mainIndex.get(index)) - yShift);
            if (sign2 == 0) newLng.set(mainIndex.get(index), newLng.get(mainIndex.get(index)) + xShift);
            else newLng.set(mainIndex.get(index), newLng.get(mainIndex.get(index)) - xShift);
            mainIndex.remove(index);
        }
        return new rawTrajectory(newLat, newLng);
    }

    void saveTrajecory(String path) {
        String[] Header = {"ID", "X", "Y"};
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        Row headerRow = sheet.createRow(0);
        for (int j = 0; j < Header.length; j++) {
            Cell headerCell = headerRow.createCell(j);
            headerCell.setCellValue(Header[j]);
        }
        for (int i = 1; i <= this.getLength(); i++) {
            Row row = sheet.createRow(i);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(i - 1);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(this.latitude.get(i - 1));
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(this.longitude.get(i - 1));
        }
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            workbook.write(outputStream);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static double dist(double[] point1, double[] point2) {
        return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
    }

    double LCSS(rawTrajectory T, double threshhold) {
        if (this.getLength() == 0 || T.getLength() == 0) return (0);
        double[][] similarityMatrix = new double[this.getLength() + 1][T.getLength() + 1];
        for (int i = 1; i <= this.getLength(); i++) {
            for (int j = 1; j <= T.getLength(); j++) {
                if (dist(this.getElement(i - 1), T.getElement(j - 1)) < threshhold) {
                    similarityMatrix[i][j] = 1 + similarityMatrix[i - 1][j - 1];
                } else similarityMatrix[i][j] = Math.max(similarityMatrix[i - 1][j], similarityMatrix[i][j - 1]);
            }
        }
        return similarityMatrix[this.getLength()][T.getLength()] / Math.max(this.getLength(), T.getLength());
    }

    private static double membership(double distance, double a, double b) {
        if (distance <= a) return 1;
        else if (distance >= b) return 0;
        return -(distance - a) / (b - a) + 1;
    }

    private static double membershipDistance(double distance, double a, double b) {
        if (distance < a) return 0;
        else if (distance > b) return 1;
        return (distance - a) / (b - a);
    }

    private static double sigmoidal_membership(double distance, double a, double b, double c) {
        if (distance <= a) return 1;
        else if (distance > a & distance <= b) return 1-2*Math.pow((distance-a)/(c-a),2);
        else if (distance > b & distance < c) return 2*Math.pow((distance-c)/(c-a),2);
        else return 0;
    }

    private static double J_shaped_membership(double distance, double a, double b) {
        if (distance <= a) return 1;
        else if (distance > b) return 0;
        return Math.pow((distance-b)/(b-a),2);
    }

    double fuzzyLCSS(rawTrajectory T, double a, double b) {
        if (this.getLength() == 0 || T.getLength() == 0) return (0);
        double[][] similarityMatrix = new double[this.getLength() + 1][T.getLength() + 1];
        for (int i = 1; i <= this.getLength(); i++) {
            for (int j = 1; j <= T.getLength(); j++) {
                double membershipDegree = membership(dist(this.getElement(i - 1), T.getElement(j - 1)), a, b);
                similarityMatrix[i][j] = Math.max(membershipDegree + similarityMatrix[i - 1][j - 1], Math.max(similarityMatrix[i - 1][j], similarityMatrix[i][j - 1]));
            }
        }
        return similarityMatrix[this.getLength()][T.getLength()] / Math.max(this.getLength(), T.getLength());
    }

    double EDR(rawTrajectory T, double threshhold) {
        if (this.getLength() == 0 || T.getLength() == 0) return (1);
        int subcost;
        double[][] similarityMatrix = new double[this.getLength() + 1][T.getLength() + 1];
        for (int i = 1; i <= this.getLength(); i++) {
            similarityMatrix[i][0] = i;
        }
        for (int i = 1; i <= T.getLength(); i++) {
            similarityMatrix[0][i] = i;
        }
        for (int i = 1; i <= this.getLength(); i++) {
            for (int j = 1; j <= T.getLength(); j++) {
                if (dist(this.getElement(i - 1), T.getElement(j - 1)) < threshhold) subcost = 0;
                else {
                    subcost = 1;
                }
                similarityMatrix[i][j] = Math.min(similarityMatrix[i - 1][j - 1] + subcost, Math.min(similarityMatrix[i][j - 1] + 1, similarityMatrix[i - 1][j] + 1));
            }
        }
        return 1-(similarityMatrix[this.getLength()][T.getLength()] / Math.max(this.getLength(), T.getLength()));
    }

    double fuzzyEDR(rawTrajectory T, int a, int b) {
        if (this.getLength() == 0 || T.getLength() == 0) return (1);
        double subcost;
        double[][] similarityMatrix = new double[this.getLength() + 1][T.getLength() + 1];
        for (int i = 1; i <= this.getLength(); i++) {
            similarityMatrix[i][0] = i;
        }
        for (int i = 1; i <= T.getLength(); i++) {
            similarityMatrix[0][i] = i;
        }
        for (int i = 1; i <= this.getLength(); i++) {
            for (int j = 1; j <= T.getLength(); j++) {
                subcost = 1 - membership(dist(this.getElement(i - 1), T.getElement(j - 1)), a, b);
                similarityMatrix[i][j] = Math.min(similarityMatrix[i - 1][j - 1] + subcost, Math.min(similarityMatrix[i][j - 1] + 1, similarityMatrix[i - 1][j] + 1));
            }
        }
        return (similarityMatrix[this.getLength()][T.getLength()] / Math.max(this.getLength(), T.getLength()));
    }

    double DTW(rawTrajectory T) {
        if (this.getLength() == 0 && T.getLength() == 0) return 0;
        else if (this.getLength() == 0 || T.getLength() == 0) return Double.POSITIVE_INFINITY;
        double[][] distanceMatrix = new double[this.getLength() + 1][T.getLength() + 1];
        for (int i = 1; i <= T.getLength(); i++) distanceMatrix[0][i] = Double.POSITIVE_INFINITY;
        for (int i = 1; i <= this.getLength(); i++) distanceMatrix[i][0] = Double.POSITIVE_INFINITY;
        for (int i = 1; i <= this.getLength(); i++) {
            for (int j = 1; j <= T.getLength(); j++) {
                distanceMatrix[i][j] = dist(this.getElement(i - 1), T.getElement(j - 1)) + Math.min(distanceMatrix[i - 1][j - 1], Math.min(distanceMatrix[i - 1][j], distanceMatrix[i][j - 1]));
            }
        }
        return (distanceMatrix[this.getLength()][T.getLength()]/Math.max(this.getLength(), T.getLength()));
    }

    double fuzzyDTW(rawTrajectory T, double a, double b){
        if (this.getLength() == 0 && T.getLength() == 0) return 0;
        else if (this.getLength() == 0 || T.getLength() == 0) return Double.POSITIVE_INFINITY;
        double[][] distanceMatrix = new double[this.getLength() + 1][T.getLength() + 1];
        for (int i = 1; i <= T.getLength(); i++) distanceMatrix[0][i] = Double.POSITIVE_INFINITY;
        for (int i = 1; i <= this.getLength(); i++) distanceMatrix[i][0] = Double.POSITIVE_INFINITY;
        for (int i = 1; i <= this.getLength(); i++) {
            for (int j = 1; j <= T.getLength(); j++) {
                double membershipDegree = membershipDistance(dist(this.getElement(i - 1), T.getElement(j - 1)), a, b);
                distanceMatrix[i][j] = membershipDegree + Math.min(distanceMatrix[i - 1][j - 1], Math.min(distanceMatrix[i - 1][j], distanceMatrix[i][j - 1]));
            }
        }
        return (distanceMatrix[this.getLength()][T.getLength()]/Math.max(this.getLength(), T.getLength()));
    }

    double ERP(rawTrajectory T, double g1, double g2) {
        double[] G = {g1, g2};
        double sum = 0;
        if (this.getLength() == 0) {
            for (int i = 0; i < T.getLength(); i++) {
                sum += dist(T.getElement(i), G);
            }
            return sum/T.getLength();
        }
        if (T.getLength() == 0) {
            for (int i = 0; i < this.getLength(); i++) {
                sum += dist(this.getElement(i), G);
            }
            return sum/this.getLength();
        }
        double[][] distanceMatrix = new double[this.getLength() + 1][T.getLength() + 1];
        for (int i = 1; i <= T.getLength(); i++)
            distanceMatrix[0][i] = distanceMatrix[0][i - 1] + dist(T.getElement(i - 1), G);
        for (int i = 1; i <= this.getLength(); i++)
            distanceMatrix[i][0] = distanceMatrix[i - 1][0] + dist(this.getElement(i - 1), G);
        for (int i = 1; i <= this.getLength(); i++) {
            for (int j = 1; j <= T.getLength(); j++) {
                distanceMatrix[i][j] = Math.min(dist(this.getElement(i - 1), T.getElement(j - 1)) + distanceMatrix[i - 1][j - 1],
                        Math.min(dist(this.getElement(i - 1), G) + distanceMatrix[i - 1][j],
                                dist(T.getElement(j - 1), G) + distanceMatrix[i][j - 1]));
            }
        }
        return (distanceMatrix[this.getLength()][T.getLength()]/Math.max(this.getLength(), T.getLength()));
    }

    double fuzzyERP(rawTrajectory T, double g1, double g2, double a, double b) {
        double[] G = {g1, g2};
        double sum = 0;
        if (this.getLength() == 0) {
            for (int i = 0; i < T.getLength(); i++) {
                sum += membershipDistance(dist(T.getElement(i), G), a, b);
            }
            return sum/T.getLength();
        }
        if (T.getLength() == 0) {
            for (int i = 0; i < this.getLength(); i++) {
                sum += membershipDistance(dist(this.getElement(i), G), a, b);
            }
            return sum/this.getLength();
        }
        double[][] distanceMatrix = new double[this.getLength() + 1][T.getLength() + 1];
        for (int i = 1; i <= T.getLength(); i++)
            distanceMatrix[0][i] = distanceMatrix[0][i - 1] + membershipDistance(dist(T.getElement(i - 1), G), a, b);
        for (int i = 1; i <= this.getLength(); i++)
            distanceMatrix[i][0] = distanceMatrix[i - 1][0] + membershipDistance(dist(this.getElement(i - 1), G), a, b);
        for (int i = 1; i <= this.getLength(); i++) {
            for (int j = 1; j <= T.getLength(); j++) {

                distanceMatrix[i][j] = Math.min(membershipDistance(dist(this.getElement(i - 1), T.getElement(j - 1)), a, b) + distanceMatrix[i - 1][j - 1],
                        Math.min(membershipDistance(dist(this.getElement(i - 1), G), a, b) + distanceMatrix[i - 1][j],
                                membershipDistance(dist(T.getElement(j - 1), G), a, b) + distanceMatrix[i][j - 1]));
            }
        }
        return (distanceMatrix[this.getLength()][T.getLength()]/Math.max(this.getLength(), T.getLength()));
    }

    double hausdorff(rawTrajectory T){
        ArrayList<Double> minA = new ArrayList<>();
        for (int i=0; i<this.getLength(); i++){
            ArrayList<Double> distanceRaw = new ArrayList<>();
            for (int j=0; j<T.getLength(); j++){
                distanceRaw.add(dist(this.getElement(i),T.getElement(j)));
            }
            minA.add(distanceRaw.get(distanceRaw.indexOf(Collections.min(distanceRaw))));
        }
        ArrayList<Double> minB = new ArrayList<>();
        for (int i=0; i<T.getLength(); i++){
            ArrayList<Double> distanceRaw = new ArrayList<>();
            for (int j=0; j<this.getLength(); j++){
                distanceRaw.add(dist(T.getElement(i),this.getElement(j)));
            }
            minB.add(distanceRaw.get(distanceRaw.indexOf(Collections.min(distanceRaw))));
        }
        double maxA = minA.get(minA.indexOf(Collections.max(minA)));
        double maxB = minB.get(minB.indexOf(Collections.max(minB)));
        return Math.max(maxA, maxB);
    }

    double frechet(rawTrajectory T){
        if (this.getLength() == 0 && T.getLength() == 0) return 0;
        else if (this.getLength() == 0 || T.getLength() == 0) return Double.POSITIVE_INFINITY;
        double[][] ca = new double[this.getLength()][T.getLength()];
        for (int i = 0; i < this.getLength(); i++){
            for (int j = 0; j < T.getLength(); j++){
                ca[i][j] = -1.0;
            }
        }
        for (int i = 0; i< this.getLength(); i++){
            for (int j = 0; j< T.getLength(); j++){
                if (ca[i][j] > -1) return ca[i][j];
                else if (i==0 && j==0) ca[i][j] = dist(this.getElement(0),T.getElement(0));
                else if (i>0 && j==0) ca[i][j] = Math.max(ca[i-1][0],dist(this.getElement(i),T.getElement(0)));
                else if (i==0 && j>0) ca[i][j] = Math.max(ca[0][j-1],dist(this.getElement(0),T.getElement(j)));
                else if (i>0 && j>0) ca[i][j] = Math.max(dist(this.getElement(i),T.getElement(j)), Math.min(ca[i-1][j-1],Math.min(ca[i-1][j],ca[i][j-1])));
                else ca [i][j] = Double.POSITIVE_INFINITY;
            }
        }
        return ca[this.getLength()-1][T.getLength()-1];
    }


    double FLCSS(rawTrajectory T, double a, double b) {
        if (this.getLength() == 0 || T.getLength() == 0) return (0);
        double[][] similarityMatrix = new double[this.getLength() + 1][T.getLength() + 1];
        String[][] directions = new String[this.getLength()][T.getLength()];

        ArrayList<Integer> match_S = new ArrayList<>();
        ArrayList<Integer> match_T = new ArrayList<>();
        ArrayList<point> non_sample_S = new ArrayList<>();
        ArrayList<point> non_sample_T = new ArrayList<>();

        for (int i = 1; i <= this.getLength(); i++) {
            for (int j = 1; j <= T.getLength(); j++) {
                double membershipDegree = membership(dist(this.getElement(i - 1), T.getElement(j - 1)), a, b);
                if (membershipDegree + similarityMatrix[i - 1][j - 1] > Math.max(similarityMatrix[i - 1][j], similarityMatrix[i][j - 1]) ) {
                    similarityMatrix[i][j] = membershipDegree + similarityMatrix[i - 1][j - 1];
                    directions[i-1][j-1] = "d";
                } else if (similarityMatrix[i - 1][j] >= similarityMatrix[i][j - 1]) {
                    similarityMatrix[i][j] = similarityMatrix[i - 1][j];
                    directions[i-1][j-1] = "u";
                } else {
                    similarityMatrix[i][j] = similarityMatrix[i][j - 1];
                    directions[i-1][j-1] = "l";
                }
            }
        }

        int indexi = this.getLength()-1;
        int indexj = T.getLength()-1;
        while(indexi>=0 & indexj>=0){
            if (directions[indexi][indexj] == "d"){
                match_S.add(indexi);
                match_T.add(indexj);
                indexi--;
                indexj--;
            }else if (directions[indexi][indexj] == "u"){
                indexi--;
            }else{
                indexj--;
            }
        }
        Collections.reverse(match_S);
        Collections.reverse(match_T);


        double similarity = similarityMatrix[this.getLength()][T.getLength()]*2;
        if (match_S.size()>1 & match_T.size()>1) {
            int k = 0;
            for (int i = 0; i < this.getLength(); i++) {
                int current_match_S = match_S.get(k);
                int current_match_T = match_T.get(k);
                int next_match_S = match_S.get(k + 1);
                int next_match_T = match_T.get(k + 1);
                if (!match_S.contains(i)) {
                    if (i < match_S.get(0)) {
                        non_sample_S.add(new point(i, "f", match_T.get(0), match_S.get(0), 99999, 99999));


                    } else if (i > match_S.get(match_S.size() - 1)) {
                        non_sample_S.add(new point(i, "l", 99999, 99999, match_T.get(match_T.size() - 1), match_S.get(match_S.size() - 1)));

                    } else {
                        non_sample_S.add(new point(i, "n", next_match_T, next_match_S, current_match_T, current_match_S));
                    }
                } else {
                    if (i == next_match_S & i != match_S.get(match_S.size() - 1)) k++;
                }
            }
            int l = 0;
            for (int i = 0; i < T.getLength(); i++) {
                int current_match_S = match_S.get(l);
                int current_match_T = match_T.get(l);
                int next_match_S = match_S.get(l + 1);
                int next_match_T = match_T.get(l + 1);
                if (!match_T.contains(i)) {
                    if (i < match_T.get(0)) {
                        non_sample_T.add(new point(i, "f", match_S.get(0), match_T.get(0), 99999, 99999));

                    } else if (i > match_T.get(match_T.size() - 1)) {
                        non_sample_T.add(new point(i, "l", 99999, 99999, match_S.get(match_S.size() - 1), match_T.get(match_T.size() - 1)));

                    } else {
                        non_sample_T.add(new point(i, "n", next_match_S, next_match_T, current_match_S, current_match_T));

                    }
                } else {
                    if (i == next_match_T & i != match_T.get(match_T.size() - 1)) l++;
                }
            }


            for (int i = 0; i < non_sample_S.size(); i++) {
                if (non_sample_S.get(i).type == "f") {
                    ArrayList<Double> matching_degrees = new ArrayList<>();
                    for (int j = 0; j <= non_sample_S.get(i).after_match; j++) {
                        double distance = dist(T.getElement(j), this.getElement(non_sample_S.get(i).index)) + dist(T.getElement(j + 1), this.getElement(non_sample_S.get(i).index)) - dist(T.getElement(j), T.getElement(j + 1));
                        matching_degrees.add(membership(distance, a, b));
                    }
                    similarity += matching_degrees.get(matching_degrees.indexOf(Collections.max(matching_degrees)));
                } else if (non_sample_S.get(i).type == "l") {
                    ArrayList<Double> matching_degrees = new ArrayList<>();
                    for (int j = T.getLength() - 1; j >= non_sample_S.get(i).before_match; j--) {
                        double distance = dist(T.getElement(j), this.getElement(non_sample_S.get(i).index)) + dist(T.getElement(j - 1), this.getElement(non_sample_S.get(i).index)) - dist(T.getElement(j), T.getElement(j - 1));
                        matching_degrees.add(membership(distance, a, b));
                    }
                    similarity += matching_degrees.get(matching_degrees.indexOf(Collections.max(matching_degrees)));
                } else if ((non_sample_S.get(i).type == "n")) {
                    ArrayList<Double> matching_degrees = new ArrayList<>();
                    for (int j = non_sample_S.get(i).before_match; j < non_sample_S.get(i).after_match; j++) {
                        double distance = dist(T.getElement(j), this.getElement(non_sample_S.get(i).index)) + dist(T.getElement(j + 1), this.getElement(non_sample_S.get(i).index)) - dist(T.getElement(j), T.getElement(j + 1));
                        matching_degrees.add(membership(distance, a, b));
                    }
                    try {
                        similarity += matching_degrees.get(matching_degrees.indexOf(Collections.max(matching_degrees)));
                    } catch (Exception e) {
                        System.out.println("asdasdasd");
                    }
                }

            }
            for (int i = 0; i < non_sample_T.size(); i++) {
                if (non_sample_T.get(i).type == "f") {
                    ArrayList<Double> matching_degrees = new ArrayList<>();
                    for (int j = 0; j <= non_sample_T.get(i).after_match; j++) {
                        double distance = dist(this.getElement(j), T.getElement(non_sample_T.get(i).index)) + dist(this.getElement(j + 1), T.getElement(non_sample_T.get(i).index)) - dist(this.getElement(j), this.getElement(j + 1));
                        matching_degrees.add(membership(distance, a, b));
                    }
                    similarity += matching_degrees.get(matching_degrees.indexOf(Collections.max(matching_degrees)));
                } else if (non_sample_T.get(i).type == "l") {
                    ArrayList<Double> matching_degrees = new ArrayList<>();
                    for (int j = this.getLength() - 1; j >= non_sample_T.get(i).before_match; j--) {
                        double distance = dist(this.getElement(j), T.getElement(non_sample_T.get(i).index)) + dist(this.getElement(j - 1), T.getElement(non_sample_T.get(i).index)) - dist(this.getElement(j), this.getElement(j - 1));
                        matching_degrees.add(membership(distance, a, b));
                    }
                    similarity += matching_degrees.get(matching_degrees.indexOf(Collections.max(matching_degrees)));
                } else if (non_sample_T.get(i).type == "n") {
                    ArrayList<Double> matching_degrees = new ArrayList<>();
                    for (int j = non_sample_T.get(i).before_match; j < non_sample_T.get(i).after_match; j++) {
                        double distance = dist(this.getElement(j), T.getElement(non_sample_T.get(i).index)) + dist(this.getElement(j + 1), T.getElement(non_sample_T.get(i).index)) - dist(this.getElement(j), this.getElement(j + 1));
                        matching_degrees.add(membership(distance, a, b));
                    }
                    try {
                        similarity += matching_degrees.get(matching_degrees.indexOf(Collections.max(matching_degrees)));
                    } catch (Exception e) {
                        System.out.println("asdasdasd12123");
                    }
                }
            }
        }
        return similarity/(this.getLength()+T.getLength());
    }

    public static void main(String[] args){

        // create synthetic trajectories
        rawTrajectory refTraj =  new rawTrajectory("D:\\kntu\\payanname\\data\\dact\\T_49.xlsx", "lat", "lng");

        
        ArrayList<Double> lat = new ArrayList<>();
        ArrayList<Double> lon = new ArrayList<>();
        Double[] latLon;
        Deg2UTM converter = new Deg2UTM();
        String[] header = {"ID", "X", "Y"};
        Double[] rate = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        Double[] distance = {0.0, 30.0, 60.0, 90.0, 120.0};
        for(int i=0; i<refTraj.getLength(); i++){
            latLon = converter.Deg2UTM(refTraj.latitude.get(i), refTraj.longitude.get(i));
            lat.add(latLon[0]);
            lon.add(latLon[1]);
        }

        rawTrajectory refTraj_utm = new rawTrajectory(lat, lon);
        for(int k=0; k<rate.length; k++) {
            for (int j = 0; j < distance.length; j++) {
                for (int i = 0; i < 100; i++) {
                    rawTrajectory noised = refTraj_utm.addNoise(rate[k], distance[j], 30);
                    noised.saveTrajecory("D:\\kntu\\payanname\\data\\dact\\T49_addNoise_" + Integer.toString((int)(rate[k]*100)) + "_" + Integer.toString((int)(distance[j] + 30)) + "_" + Integer.toString(i) + ".xlsx", header);
                }
            }
        }

    }
}


