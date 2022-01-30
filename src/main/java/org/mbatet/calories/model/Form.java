package org.mbatet.calories.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Form {

    private static final Logger log = LoggerFactory.getLogger(Form.class);

    private String textArea = "";
    private MultipartFile file;

    //boolean printComments = false;



    /*

    public final static String SELECTS_WITH_COMMENTS = "SELECTS_WITH_COMMENTS";
    public final static String SELECTS_WITHOUT_COMMENTS = "SELECTS_WITHOUT_COMMENTS";
    */


    /**
     * 0: Updates amb comentaris
     * 1: Updates sense comentaris
     * 2: Selects amb comentaris
     * 3: Selects sense comentaris
     * */
    String type = Constants.WHEIGHT_TRACKING_CHART;

    public Form(){

    }

    public MultipartFile getFile() {return file;}
    public void setFile(MultipartFile file) {this.file = file;}




    public String getTextArea() {return textArea;}
    public void setTextArea(String textArea) {
        if(textArea!=null){
            this.textArea = textArea.trim();
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEmpty()
    {
        if( this.file == null && ( this.textArea == null || this.textArea.equals("") ) )
        {
            return true;
        }

        return false;
    }

    public String getText() throws IOException {

        if( this.textArea!=null && !this.textArea.equals("")) {
            log.info("[m:getText] Retornem contingut del textarea...");
            return this.textArea;
        }

        log.info("[m:getText] Retornem contingut del fitxer: "+ this.file.getOriginalFilename());
        //String content = new String(multipartFile.getBytes());
        return new String(this.file.getBytes(), StandardCharsets.UTF_8);
    }




    public String toString()
    {
        return "empty:" + this.isEmpty() + ".type:" + this.type;
    }

}
