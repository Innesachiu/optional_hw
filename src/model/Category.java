package model;
/** Category entity. */
public class Category { private int categoryId; private String name;
/** Default constructor. */ public Category() {}
/** Full constructor. */ public Category(int categoryId,String name){this.categoryId=categoryId;this.name=name;}
/** @return id */ public int getCategoryId(){return categoryId;} /** @param categoryId id */ public void setCategoryId(int categoryId){this.categoryId=categoryId;}
/** @return name */ public String getName(){return name;} /** @param name name */ public void setName(String name){this.name=name;}
}
