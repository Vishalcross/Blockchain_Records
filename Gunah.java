import java.io.Serializable;

// import java.util.ArrayList;
// import java.util.Random;
class Gunah implements Serializable{
    private static final long serialVersionUID = -806001726723318406L;
    String crime;
    int idNo;
    Gunah(int idNo, String gunah){
        this.idNo = idNo;
        this.crime = gunah;
    }
}