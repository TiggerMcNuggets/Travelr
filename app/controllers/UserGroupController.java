package controllers;

import play.data.FormFactory;
import play.mvc.Controller;
import repository.UserGroupRepository;

import javax.inject.Inject;

public class UserGroupController extends Controller {

    @Inject
    FormFactory formFactory;

    private final UserGroupRepository userGroupRepository;

    @Inject
    public UserGroupController(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }



}
