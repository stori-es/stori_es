/**
 * Copyright 2015 ArcBees Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.consumersunion.stories.common.client.resource;

import com.arcbees.chosen.client.resources.ChozenCss;
import com.arcbees.chosen.client.resources.Resources;

public interface ChosenResources extends Resources {
    interface CustomChosenCss extends ChozenCss {
    }

    @Override
    @Source({"com/arcbees/gsss/mixin/client/mixins.gss",
            "com/arcbees/chosen/client/resources/colors.gss",
            "com/arcbees/chosen/client/resources/icons/icons.gss",
            "css/chozen.gss"})
    CustomChosenCss css();
}
