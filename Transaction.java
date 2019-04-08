class Transaction{
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