package service;
import dao.SearchLogDAO;import java.util.*;
/** Service for search logs. */
public class SearchService { private final SearchLogDAO dao=new SearchLogDAO();
    /** @param userId user id @param keyword keyword */ public void logSearch(Integer userId,String keyword){dao.logKeyword(userId,keyword);} 
    /** @return top keywords */ public List<String> popularKeywords(){return dao.topKeywordsLast7Days();}
}
