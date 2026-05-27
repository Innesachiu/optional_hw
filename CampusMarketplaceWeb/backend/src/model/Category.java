package model;

/**
 * Category entity mapping categories table.
 */
public class Category {
    private int categoryId;
    private String name;

    /** Default constructor. */
    public Category() {}
    /** @return category id */
    public int getCategoryId() { return categoryId; }
    /** @param categoryId category id */
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    /** @return category name */
    public String getName() { return name; }
    /** @param name category name */
    public void setName(String name) { this.name = name; }
}
