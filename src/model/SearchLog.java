package model;
/** Search log entity. */
public class SearchLog { private int searchId; private Integer userId; private String keyword;
/** Default constructor. */ public SearchLog() {}
/** @return id */ public int getSearchId(){return searchId;} /** @param v id */ public void setSearchId(int v){searchId=v;}
/** @return user id */ public Integer getUserId(){return userId;} /** @param v user id */ public void setUserId(Integer v){userId=v;}
/** @return keyword */ public String getKeyword(){return keyword;} /** @param v keyword */ public void setKeyword(String v){keyword=v;}
}
