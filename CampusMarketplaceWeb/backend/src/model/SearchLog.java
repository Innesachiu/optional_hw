package model;

import java.sql.Timestamp;

/**
 * SearchLog entity mapping search_logs table.
 */
public class SearchLog {
    private int searchId;
    private Integer userId;
    private String keyword;
    private Timestamp createdAt;

    /** Default constructor. */
    public SearchLog() {}
    /** @return search id */
    public int getSearchId() { return searchId; }
    /** @param searchId search id */
    public void setSearchId(int searchId) { this.searchId = searchId; }
    /** @return user id */
    public Integer getUserId() { return userId; }
    /** @param userId user id */
    public void setUserId(Integer userId) { this.userId = userId; }
    /** @return keyword */
    public String getKeyword() { return keyword; }
    /** @param keyword keyword */
    public void setKeyword(String keyword) { this.keyword = keyword; }
    /** @return created time */
    public Timestamp getCreatedAt() { return createdAt; }
    /** @param createdAt created time */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
