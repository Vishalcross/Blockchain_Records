import java.io.Serializable;

class Transaction implements Serializable{
	private static final long serialVersionUID = -1577121386678663199L;
	Gunah gunah;
	Transaction(int criminalId, String crime){
		this.gunah = new Gunah(criminalId,crime);
	}
	String getString(){
		return ""+gunah.idNo+gunah.crime;
	}
	String getHash(){
		return StringUtil.applySha256(""+this.gunah.idNo+this.gunah.crime);
	}
}