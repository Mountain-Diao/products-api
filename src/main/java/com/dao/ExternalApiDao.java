package com.dao;

import java.io.IOException;

public interface ExternalApiDao {
    boolean callOrdersApi(long id) throws IOException;
}
