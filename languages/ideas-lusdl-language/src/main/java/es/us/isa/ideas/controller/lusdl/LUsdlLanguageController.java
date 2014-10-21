package es.us.isa.ideas.controller.lusdl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.linkedusdl.agreement.mapping.AmazonUSDL2iAgreeMapper;
import org.linkedusdl.agreement.mapping.USDLModel;
import org.linkedusdl.agreement.mapping.WriteiAgreeFromiAgreeModel;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.openrdf.rio.RDFParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viceversatech.rdfbeans.exceptions.RDFBeanException;

import es.us.isa.ada.wsag10.Agreement;
import es.us.isa.ideas.common.AppAnnotations;
import es.us.isa.ideas.common.AppResponse;
import es.us.isa.ideas.common.AppResponse.Status;
import es.us.isa.ideas.module.controller.BaseLanguageController;
import es.us.isa.ideas.utils.repolab.RepoLab;

@Controller
@RequestMapping("/language")
public class LUsdlLanguageController extends BaseLanguageController {
	
	@RequestMapping(value = "/operation/{id}/execute", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse executeOperation(String id, String content, String fileUri, Map<String, String> data) {
        AppResponse ret = new AppResponse();
		String newURI = constructUri(fileUri);
        if(id.equals("transform2iAgree")){
        	try{
        		File f = new File(RepoLab.get().getRepoBaseUri()+"/dani8art/"+fileUri);
	        	USDLModel model = new USDLModel(new FileInputStream(f));
				AmazonUSDL2iAgreeMapper mapping = new AmazonUSDL2iAgreeMapper(model);
				WriteiAgreeFromiAgreeModel writer = new WriteiAgreeFromiAgreeModel();
				Agreement ag = mapping.transform();
				writer.writeFile(ag, mapping.getCC(),mapping.getMetrics(), RepoLab.get().getRepoBaseUri()+"/dani8art/"+newURI);
				ret.setFileUri(newURI);
			    ret.setMessage("Transformation correct. New file at: "+newURI);
			    ret.setStatus(Status.OK);
        	}catch(IOException e){
        		ret.setFileUri(fileUri);
			    ret.setMessage("There are problems whit I/O File");
			    ret.setStatus(Status.OK_PROBLEMS);
        	}catch(RDFBeanException e){
        		ret.setFileUri(fileUri);
			    ret.setMessage("There are problems whit the syntax: RDFBeansExc");
			    ret.setStatus(Status.OK_PROBLEMS);
			}catch(ModelRuntimeException e){
				ret.setFileUri(fileUri);
			    ret.setMessage("There are problems whit the syntax: ModelRuntimeException: "+ e.getMessage());
			    ret.setStatus(Status.OK_PROBLEMS);
			}catch(RDFParseException e){
				ret.setFileUri(fileUri);
			    ret.setMessage("There are problems whit the syntax: RDFParseExcep");
			    ret.setStatus(Status.OK_PROBLEMS);	
			}catch(NullPointerException e){
				 
			}
        }
        
        return ret;
    }

    @RequestMapping(value = "/format/{format}/checkLanguage", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse checkLanguage(String format, String content, String fileUri) {
    	AppResponse ret = new AppResponse();
    	try {
    		File f = new File(RepoLab.get().getRepoBaseUri()+"/dani8art/"+fileUri);
			USDLModel model = new USDLModel(new FileInputStream(f));
			ret.setFileUri(fileUri);
		    ret.setMessage("");
		    ret.setStatus(Status.OK);
		} catch (ModelRuntimeException e) {
			ret.setFileUri(fileUri);
		    ret.setMessage("There are problems whit the syntax: ModelRuntimeException");
		    ret.setStatus(Status.OK_PROBLEMS);
		    String mesg = e.getMessage();
		    String line = mesg.substring(mesg.indexOf("["),mesg.indexOf("]")).split(" ")[1];
		    Integer row = new Integer(line);
		    row--;
		    AppAnnotations ano = new AppAnnotations();
		    ano.setRow(row.toString());
		    ano.setColumn("10");
		    ano.setText(mesg);
		    ano.setType("error");
		    AppAnnotations[] annotations = new AppAnnotations[1];
		    annotations[0]=ano;
		    ret.setAnnotations(annotations);
		} catch (RDFParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ret;
    }

    @RequestMapping(value = "/convert", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse convertFormat(String currentFormat, String desiredFormat, String fileUri, String content) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private String constructUri(String uri){
    	String ret ="";
    	String[] listUri = uri.split("/");
    	String name = listUri[listUri.length-1];
    	String[] namesplit = name.split("ttl");
    	for( int i=0; i < listUri.length-1; i++ ){
    		ret += listUri[i] + "/";
    	}
    	
    	ret += namesplit[0] + "iagreetemplate";
    	return ret;
    }
}
