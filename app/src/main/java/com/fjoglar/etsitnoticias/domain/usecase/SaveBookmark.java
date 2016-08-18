/*
 * Copyright (C) 2016 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fjoglar.etsitnoticias.domain.usecase;

import com.fjoglar.etsitnoticias.domain.UseCase;
import com.fjoglar.etsitnoticias.data.entities.NewsItem;
import com.fjoglar.etsitnoticias.data.repository.NewsDataSource;

public class SaveBookmark extends UseCase<SaveBookmark.RequestValues, SaveBookmark.ResponseValue> {

    private final NewsDataSource mNewsDataSource;

    public SaveBookmark(NewsDataSource mNewsDataSource) {
        this.mNewsDataSource = mNewsDataSource;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        // Save the bookmarks to the db.
        mNewsDataSource.saveBookmark(requestValues.getNewsItem());
        // Update bookmarked status in news table item.
        mNewsDataSource.updateNewsItemIsBookmarkedStatusByDate(true,
                requestValues.getNewsItemFormattedDate());

        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final NewsItem mNewsItem;

        public RequestValues(NewsItem newsItem) {
            this.mNewsItem = newsItem;
        }

        public NewsItem getNewsItem() {
            return mNewsItem;
        }

        public long getNewsItemFormattedDate() {
            return mNewsItem.getFormattedPubDate();
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {}

}
