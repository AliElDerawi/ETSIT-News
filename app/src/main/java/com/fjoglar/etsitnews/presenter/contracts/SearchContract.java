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
package com.fjoglar.etsitnews.presenter.contracts;

import com.fjoglar.etsitnews.model.entities.NewsItem;
import com.fjoglar.etsitnews.presenter.BasePresenter;
import com.fjoglar.etsitnews.view.BaseView;

import java.util.List;

public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showSearchedNews(List<NewsItem> newsItemList);

        void showProgress();

        void hideProgress();

        void showError();

    }

    interface Presenter extends BasePresenter {

        void performSearch(String query);

    }

}
