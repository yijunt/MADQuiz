package edu.unsw.infs.assignment.textbook;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by June on 7/10/2017.
 *
 * References:
 * 1. Read data from excel file:
 *  http://bethecoder.com/applications/tutorials/excel/jexcel-api/how-to-read-contents-of-all-cells-in-excel-spreadsheet.html
 *
 * 2. Download jxl.jar from:
 *  https://sourceforge.net/projects/jexcel/
 */

public class TopicModel {
    private String titleNum, title;

    public TopicModel() {
    }

    public TopicModel(String titleNum, String title) {
        this.titleNum = titleNum;
        this.title = title;
    }

    public String getTitleNum() {
        return titleNum;
    }

    public void setTitleNum(String titleNum) {
        this.titleNum = titleNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * A TextBook item representing a piece of content.
     */
    public static class TopicItem {
        private String subTopicid;
        private String topicId;
        private String subTopic, link;
        private String details;

        public TopicItem() {
        }

        public TopicItem(String subTopicid, String topicId, String subTopic, String link, String details) {
            this.subTopicid = subTopicid;
            this.topicId = topicId;
            this.subTopic = subTopic;
            this.link = link;
            this.details = details;
        }

        public void setSubTopicid(String subTopicid) {
            this.subTopicid = subTopicid;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public void setSubTopic(String subTopic) {
            this.subTopic = subTopic;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getSubTopicid() {
            return subTopicid;
        }

        public String getTopicId() {
            return topicId;
        }

        public String getSubTopic() {
            return subTopic;
        }

        public String getLink() {
            return link;
        }

        public String getDetails() {
            return details;
        }

        //Read data from excel file and store into TopicItem
        public TopicItem getTopicItemSet(Context mContext, String layoutPosition, String topicID) {
            TopicItem topicItem = new TopicItem();

            try {
                AssetManager assetManager = mContext.getAssets();
                InputStream inputStream = assetManager.open("TextBookData.xls");
                Workbook workbook = Workbook.getWorkbook(inputStream);

                Sheet sheet = workbook.getSheet(0);
                int row = sheet.getRows();
                for (int i = 0; i < row; i++) {
                    Cell t = sheet.getCell(0, i);
                    if (topicID.equals(t.getContents())) {
                        topicItem.setTopicId(t.getContents());
                        topicItem.setSubTopicid(sheet.getCell(1, i).getContents());
                        if (topicItem.getSubTopicid().equals(layoutPosition)) {
                            topicItem.setSubTopic(sheet.getCell(2, i).getContents());
                            topicItem.setDetails(sheet.getCell(3, i).getContents());
                            topicItem.setLink(sheet.getCell(4, i).getContents());

                            Log.d("TopicItem", "Get data from excel file and assign into topicItem");
                            break;
                        }
                    }
                }
            } catch (IOException | BiffException e) {
                Log.e("TopicItem", "Error reading excel file" + e);
            } catch (NullPointerException npe) {
                Log.e("TopicItem", "Null Pointer Exception" + npe);
            }
            return topicItem;
        }

    }
}

