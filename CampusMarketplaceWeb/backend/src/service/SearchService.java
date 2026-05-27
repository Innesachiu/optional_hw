package service;

import dao.SearchLogDAO;

import java.util.List;

/**
 * Handles search analytics business logic.
 */
public class SearchService {
    private final SearchLogDAO searchLogDAO = new SearchLogDAO();

    /**
     * Logs one search keyword.
     *
     * @param userId user id nullable
     * @param keyword keyword
     */
    public void logKeyword(Integer userId, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            searchLogDAO.log(userId, keyword.trim());
        }
    }

    /**
     * Returns top keywords in last 7 days.
     *
     * @return keyword list
     */
    public List<String> getHotKeywords() {
        return searchLogDAO.popularKeywordsLast7Days();
    }
}
