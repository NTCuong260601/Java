package Model;

import java.text.DecimalFormat;

public class Grades {

    private int id;
    private String idST;
    private float gradeE, gradeIM, gradeP;

    public Grades() {
    }

    public Grades(int id, String idST, float gradeE, float gradeIM, float gradeP) {
        this.id = id;
        this.idST = idST;
        this.gradeE = gradeE;
        this.gradeIM = gradeIM;
        this.gradeP = gradeP;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdST() {
        return idST;
    }

    public void setIdST(String idST) {
        this.idST = idST;
    }

    public float getGradeE() {
        return gradeE;
    }

    public void setGradeE(float gradeE) {
        this.gradeE = gradeE;
    }

    public float getGradeIM() {
        return gradeIM;
    }

    public void setGradeIM(float gradeIM) {
        this.gradeIM = gradeIM;
    }

    public float getGradeP() {
        return gradeP;
    }

    public void setGradeP(float gradeP) {
        this.gradeP = gradeP;
    }

    @Override
    public String toString() {
        return "Grades{" + "id=" + id + ", idST=" + idST + ", gradeE=" + gradeE + ", gradeIM=" + gradeIM + ", gradeP=" + gradeP + '}';
    }
    
    public String getDTB(float gradeE, float gradeIM, float gradeP){
        float dtb = (gradeE+gradeIM+gradeP)/3;
        DecimalFormat df = new DecimalFormat("#.##");
        String dtb1 = df.format(dtb);
        return dtb1;
    }

}
