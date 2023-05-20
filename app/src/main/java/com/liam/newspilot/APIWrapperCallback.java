package com.liam.newspilot;

import java.util.ArrayList;

public interface APIWrapperCallback {
    void onAPIWrapperPostExecute(ArrayList<Article> articles);
}
