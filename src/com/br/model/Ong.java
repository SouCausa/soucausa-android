package com.br.model;

import com.br.data.DataContract;
import com.br.data.OngContract;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Ong {
	
    public Long ONGId;
    public String RazaoSocial;
    public String NomeFantasia;
    public String CNPJ;
    public String Rua;
    public String Numero;
    public String Complemento;
    public String Cidade;
    public String UF;
    public String Bairro;
    public String Cep;
    public String Expediente;
    public String Telefone;
    public String Email;
    public String Fax;
    public String Site;
    public String DataFundacao;
    public String CRCE;
    public String PublicoAlvo;
    public String AreaAtuacao;
    public String Equipe;
    public int Habilitada;
    
	public int getHabilitada() {
		return Habilitada;
	}

	public void setHabilitada(int habilitada) {
		values.put(OngContract.COLUMN_NAME_HABILITADA, habilitada);
		Habilitada = habilitada;
	}

	protected ContentValues values;
	protected SQLiteDatabase db;
	public DataContract dbHelper;
	
	
	public Ong(Context context)
	{
		this.dbHelper = new DataContract(context);		
		values = new ContentValues();
	}
	
	public Long getONGId() {
		return ONGId;
	}
	public void setONGId(Long id) {
		values.put(OngContract.COLUMN_NAME_ONG_ID, id);
		ONGId = id;
	}
	public String getRazaoSocial() {
		return RazaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		values.put(OngContract.COLUMN_NAME_RAZAO_SOCIAL, razaoSocial);
		RazaoSocial = razaoSocial;
	}
	public String getNomeFantasia() {
		return NomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		values.put(OngContract.COLUMN_NAME_NOME_FANTASIA, nomeFantasia);
		NomeFantasia = nomeFantasia;
	}
	public String getCNPJ() {
		return CNPJ;
	}
	public void setCNPJ(String CNPJ) {
		values.put(OngContract.COLUMN_NAME_CNPJ, CNPJ);
		this.CNPJ = CNPJ;
	}
	public String getRua() {
		return Rua;
	}
	public void setRua(String rua) {
		values.put(OngContract.COLUMN_NAME_RUA, rua);
		Rua = rua;
	}
	public String getNumero() {
		return Numero;
	}
	public void setNumero(String numero) {
		values.put(OngContract.COLUMN_NAME_NUMERO, numero);
		Numero = numero;
	}
	public String getComplemento() {
		return Complemento;
	}
	public void setComplemento(String complemento) {
		values.put(OngContract.COLUMN_NAME_COMPLEMENTO, complemento);
		Complemento = complemento;
	}
	public String getCidade() {
		return Cidade;
	}
	public void setCidade(String cidade) {
		values.put(OngContract.COLUMN_NAME_CIDADE, cidade);
		Cidade = cidade;
	}
	public String getUF() {
		return UF;
	}
	public void setUF(String uF) {
		values.put(OngContract.COLUMN_NAME_UF, uF);
		UF = uF;
	}
	public String getBairro() {
		return Bairro;
	}
	public void setBairro(String bairro) {
		values.put(OngContract.COLUMN_NAME_BAIRRO, bairro);
		Bairro = bairro;
	}
	public String getCep() {
		return Cep;
	}
	public void setCep(String cep) {
		values.put(OngContract.COLUMN_NAME_CEP, cep);
		Cep = cep;
	}
	public String getExpediente() {
		return Expediente;
	}
	public void setExpediente(String expediente) {
		values.put(OngContract.COLUMN_NAME_EXPEDIENTE, expediente);
		Expediente = expediente;
	}
	public String getTelefone() {
		return Telefone;
	}
	public void setTelefone(String telefone) {
		values.put(OngContract.COLUMN_NAME_TELEFONE, telefone);
		Telefone = telefone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		values.put(OngContract.COLUMN_NAME_EMAIL, email);
		Email = email;
	}
	public String getFax() {
		return Fax;
	}
	public void setFax(String fax) {
		values.put(OngContract.COLUMN_NAME_FAX, fax);
		Fax = fax;
	}
	public String getSite() {
		return Site;
	}
	public void setSite(String site) {
		values.put(OngContract.COLUMN_NAME_SITE, site);
		Site = site;
	}
	public String getData_fundacao() {
		return DataFundacao;
	}
	
	public void setData_fundacao(String data_fundacao) {
		values.put(OngContract.COLUMN_NAME_DATA_FUNDACAO, data_fundacao);
		DataFundacao = data_fundacao;
	}
	public String getCRCE() {
		return CRCE;
	}
	public void setCRCE(String cRCE) {
		values.put(OngContract.COLUMN_NAME_CRCE, cRCE);
		CRCE = cRCE;
	}
	public String getPublico_alvo() {
		return PublicoAlvo;
	}
	public void setPublico_alvo(String publico_alvo) {
		values.put(OngContract.COLUMN_NAME_PUBLICO_ALVO, publico_alvo);
		PublicoAlvo = publico_alvo;
	}
	public String getArea_atuacao() {
		return AreaAtuacao;
	}
	public void setArea_atuacao(String area_atuacao) {
		values.put(OngContract.COLUMN_NAME_AREA_ATUACAO, area_atuacao);
		AreaAtuacao = area_atuacao;
	}
	public String getEquipe() {
		return Equipe;
	}
	public void setEquipe(String equipe) {
		values.put(OngContract.COLUMN_NAME_EQUIPE, equipe);
		Equipe = equipe;
	}
	public ContentValues getValues() {
		return values;
	}
	public void setValues(ContentValues values) {
		this.values = values;
	}
	public SQLiteDatabase getDb() {
		return db;
	}
	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}
	
	public long save()
	{		
		this.db = dbHelper.getWritableDatabase();
		long ret =  this.db.insert(OngContract.TABLE_NAME, "", values);
		this.db.close();
		
		return ret;
	}
	
}
