package com.br.soucausa.model;

import java.io.File;

import android.os.Parcel;
import android.os.Parcelable;

public class Cupom implements Parcelable{
	public Long id;
	public String CNPJ;
	public String data;
	public String COO;
	public String causaId;
	public float total;
	public String image;
	public Ong ong;
	public File file;
	
	
	
	public Cupom(){}
	
	

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public void setCausaId(String id)
	{
		this.causaId = id;
	}
	
	public String getCausaId() {
		return this.causaId;
	}

	public Ong getOng() {
		return ong;
	}

	public void setOng(Ong ong) {
		this.ong = ong;
	}

	public String getCNPJ() {
		return CNPJ;
	}
	
	public void setCNPJ(String cNPJ) {
		CNPJ = cNPJ;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getCOO() {
		return COO;
	}
	
	public void setCOO(String cOO) {
		COO = cOO;
	}
	
	public float getTotal() {
		return total;
	}
	
	public void setTotal(float total) {
		this.total = total;
	}
	
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(file);
	}
	
	public static final Parcelable.Creator<Cupom> CREATOR = new Parcelable.Creator<Cupom>() {

		@Override
		public Cupom createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Cupom();
		}

		@Override
		public Cupom[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Cupom[size];
		}
	};
}
