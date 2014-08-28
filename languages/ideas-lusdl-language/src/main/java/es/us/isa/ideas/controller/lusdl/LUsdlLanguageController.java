package es.us.isa.ideas.controller.lusdl;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.isa.ideas.common.AppResponse;
import es.us.isa.ideas.module.controller.BaseLanguageController;

public class LUsdlLanguageController extends BaseLanguageController {
	
	@RequestMapping(value = "/operation/{id}/execute", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse executeOperation(String id, String content, String fileUri, Map<String, String> data) {
        // TODO Auto-generated method stub
        return null;
    }

    @RequestMapping(value = "/format/{format}/checkLanguage", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse checkLanguage(String format, String content, String fileUri) {
        // TODO Auto-generated method stub
        return null;
    }

    @RequestMapping(value = "/convert", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse convertFormat(String currentFormat, String desiredFormat, String fileUri, String content) {
        // TODO Auto-generated method stub
        return null;
    }
}
