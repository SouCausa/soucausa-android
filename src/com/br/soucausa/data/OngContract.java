package com.br.soucausa.data;

import android.provider.BaseColumns;

public abstract class OngContract implements BaseColumns {
    
	//id,razao_social,nome_fantasia,cnpj,rua,numero,complemento,cidade,uf,bairro,cep,expediente,
	//telefone,email,fax,site,data_fundacao,crce,publico_alvo,area_atuacao,equipe
	public static final String TABLE_NAME = "Ong";
    
    public static final String COLUMN_NAME_ONG_ID = "_id";
    public static final String COLUMN_NAME_RAZAO_SOCIAL = "razao_social";
    public static final String COLUMN_NAME_NOME_FANTASIA = "nome_fantasia";
    public static final String COLUMN_NAME_CNPJ = "cnpj";
    public static final String COLUMN_NAME_RUA = "rua";
    public static final String COLUMN_NAME_NUMERO = "numero";
    public static final String COLUMN_NAME_COMPLEMENTO = "complemento";
    public static final String COLUMN_NAME_CIDADE = "cidade";
    public static final String COLUMN_NAME_UF = "uf";
    public static final String COLUMN_NAME_BAIRRO = "bairro";
    public static final String COLUMN_NAME_CEP= "cep";
    public static final String COLUMN_NAME_EXPEDIENTE = "expediente";
    public static final String COLUMN_NAME_TELEFONE = "telefone";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_FAX = "fax";
    public static final String COLUMN_NAME_SITE = "site";
    public static final String COLUMN_NAME_DATA_FUNDACAO = "data_fundacao";
    public static final String COLUMN_NAME_CRCE = "crce";
    public static final String COLUMN_NAME_PUBLICO_ALVO = "publico_alvo";
    public static final String COLUMN_NAME_AREA_ATUACAO = "area_atuacao";
    public static final String COLUMN_NAME_EQUIPE = "equipe";
    public static final String COLUMN_NAME_HABILITADA = "habilitada";

}
