package org.consumersunion.stories.server.api.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.QuestionnaireResourceLinks;
import org.consumersunion.stories.common.shared.dto.QuestionnaireResponse;
import org.consumersunion.stories.common.shared.dto.QuestionnairesApiResponse;
import org.consumersunion.stories.common.shared.dto.post.QuestionnairePost;
import org.consumersunion.stories.common.shared.dto.post.QuestionnairePut;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireBase;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.datatransferobject.QuestionnaireData;
import org.consumersunion.stories.server.api.rest.converters.QuestionnaireConverter;
import org.consumersunion.stories.server.api.rest.merger.QuestionnairePutMerger;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.business_logic.QuestionnaireService;
import org.consumersunion.stories.server.business_logic.TagsService;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.api.EndPoints.COLLECTIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.DOCUMENTS;
import static org.consumersunion.stories.common.shared.api.EndPoints.ORGANIZATIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.QUESTIONNAIRES;
import static org.consumersunion.stories.common.shared.api.EndPoints.STORIES;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;

@Component
@Path(EndPoints.QUESTIONNAIRES)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuestionnairesResource {
    private final TagsService tagsService;
    private final QuestionnaireService questionnaireService;
    private final QuestionnaireConverter questionnaireConverter;
    private final QuestionnairePutMerger questionnairePutMerger;
    private final ResourceLinksHelper resourceLinksHelper;

    @Inject
    QuestionnairesResource(
            TagsService tagsService,
            QuestionnaireService questionnaireService,
            QuestionnaireConverter questionnaireConverter,
            QuestionnairePutMerger questionnairePutMerger,
            ResourceLinksHelper resourceLinksHelper) {
        this.tagsService = tagsService;
        this.questionnaireService = questionnaireService;
        this.questionnaireConverter = questionnaireConverter;
        this.questionnairePutMerger = questionnairePutMerger;
        this.resourceLinksHelper = resourceLinksHelper;
    }

    @GET
    @Path(EndPoints.ID)
    public Response getQuestionnaire(@PathParam(UrlParameters.ID) int id) {
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(id);

        QuestionnairesApiResponse apiResponse = createQuestionnairesResponse(new QuestionnaireData(questionnaire));

        return Response.ok(apiResponse).build();
    }

    @PUT
    @Path(EndPoints.ID)
    public Response updateQuestionnaire(@PathParam(UrlParameters.ID) int id, QuestionnairePut questionnairePut) {
        QuestionnaireI15d questionnaire = questionnaireService.getQuestionnaireI15d(id);

        questionnairePutMerger.merge(questionnaire, questionnairePut);

        QuestionnaireI15d updatedQuestionnaire = questionnaireService.updateQuestionnaire(questionnaire);

        Set<String> tags = questionnairePut.getTags();
        tagsService.setTags(updatedQuestionnaire, tags);

        Set<String> autoTags = questionnairePut.getAutoTags();
        tagsService.setAutoTags(updatedQuestionnaire, autoTags);

        QuestionnaireData questionnaireData = new QuestionnaireData(updatedQuestionnaire, tags, autoTags);

        return Response.ok(createQuestionnairesResponse(questionnaireData)).build();
    }

    @DELETE
    @Path(EndPoints.ID)
    public Response deleteQuestionnaire(@PathParam(UrlParameters.ID) int id) {
        questionnaireService.deleteQuestionnaire(id);

        return Response.noContent().build();
    }

    @POST
    public Response createQuestionnaire(QuestionnairePost questionnairePost) {
        QuestionnaireI15d questionnaire = questionnaireConverter.convert(questionnairePost);

        QuestionnaireI15d savedQuestionnaire = questionnaireService.createQuestionnaire(questionnaire);

        Set<String> tags = questionnairePost.getTags();
        Set<String> autoTags = questionnairePost.getAutoTags();
        tagsService.setTags(savedQuestionnaire, tags);
        tagsService.setAutoTags(savedQuestionnaire, autoTags);

        QuestionnaireData questionnaireData = new QuestionnaireData(savedQuestionnaire, tags, autoTags);

        return Response.ok(createQuestionnairesResponse(questionnaireData)).build();
    }

    private QuestionnairesApiResponse createQuestionnairesResponse(QuestionnaireData... questionnaires) {
        QuestionnairesApiResponse response = new QuestionnairesApiResponse();
        response.setData(toQuestionnaireResponse(questionnaires));
        return response;
    }

    private List<QuestionnaireResponse> toQuestionnaireResponse(QuestionnaireData... questionnaires) {
        return toQuestionnaireResponse(Arrays.asList(questionnaires));
    }

    private List<QuestionnaireResponse> toQuestionnaireResponse(List<QuestionnaireData> questionnaires) {
        return Lists.transform(questionnaires, new Function<QuestionnaireData, QuestionnaireResponse>() {
            @Override
            public QuestionnaireResponse apply(QuestionnaireData questionnaireData) {
                QuestionnaireResponse response = new QuestionnaireResponse();

                QuestionnaireBase questionnaireBase = questionnaireData.getCollection();
                response.setId(questionnaireBase.getId());
                response.setCreatedOn(questionnaireBase.getCreated());
                response.setUpdatedOn(questionnaireBase.getUpdated());
                response.setTitle(questionnaireBase.getTitle());
                response.setSummary(questionnaireBase.getSummary());
                response.setArchived(questionnaireBase.getDeleted());
                response.setLinks(generateLinks(questionnaireBase));
                response.setTags(questionnaireData.getTags());
                response.setAutoTags(questionnaireData.getAutoTags());
                response.setPublished(questionnaireBase.isPublished());
                response.setPublishedOn(questionnaireBase.getPublishedDate());

                return response;
            }
        });
    }

    private QuestionnaireResourceLinks generateLinks(QuestionnaireBase questionnaire) {
        QuestionnaireResourceLinks questionnairesResourceLinks = new QuestionnaireResourceLinks();

        // TODO : Add missing links
        questionnairesResourceLinks.setCollections(
                resourceLinksHelper.replaceIntIds(endsWithId(COLLECTIONS), questionnaire.getTargetCollections()));
        questionnairesResourceLinks.setOwner(
                resourceLinksHelper.replaceId(endsWithId(ORGANIZATIONS), questionnaire.getOwner()));
        questionnairesResourceLinks.setDefaultForm(
                resourceLinksHelper.replaceId(endsWithId(DOCUMENTS), questionnaire.getSurvey().getId()));
        questionnairesResourceLinks.setForms(
                resourceLinksHelper.replaceIds(endsWithId(DOCUMENTS), Lists.newArrayList(questionnaire.getSurvey())));

        Set<StoryLink> stories = questionnaire.getStories();
        if (stories != null) {
            questionnairesResourceLinks.setStories(
                    resourceLinksHelper.replaceIntIds(endsWithId(STORIES), convertStoryLinks(stories)));
        }

        return questionnairesResourceLinks;
    }

    private Iterable<Integer> convertStoryLinks(Set<StoryLink> stories) {
        return Iterables.transform(stories, new Function<StoryLink, Integer>() {
            @Override
            public Integer apply(StoryLink input) {
                return input.getStory();
            }
        });
    }
}
