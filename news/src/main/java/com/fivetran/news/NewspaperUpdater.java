package com.fivetran.news;

import com.fivetran.news.core.RecordUploader;

public class NewspaperUpdater {
    /**
     * Main connector logic. Should sync up to today and save state for subsequent syncs.
     * @param uploader interfaces with the Core
     * @param api interfaces with the Source
     * @param state is saved for subsequent syncs
     */
    public void update(RecordUploader uploader, RealNewspaperApi api, NewspaperState state) {

    }
}
