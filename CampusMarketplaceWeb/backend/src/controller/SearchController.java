package controller;

import service.SearchService;

import java.util.List;

/**
 * Controller for search APIs.
 */
public class SearchController {
    private final SearchService searchService = new SearchService();

    /**
     * Handles GET /api/search/hot-keywords.
     *
     * @return keyword list
     */
    public List<String> getHotKeywords() {
        return searchService.getHotKeywords();
    }
}
