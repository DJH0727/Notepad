package org.example.notepad;

public class Launcher
{
    public static void main (String [] args)
    {
        Main.main(args);
    }
}





/*
究极废案

    public void FindIfChange()
    {
        if (!textArea.getSelectedText().isEmpty()) {


            String searchText = searchField.getText();
            String initText=searchText;
            // 如果选择了全词匹配
            if (Word.isSelected())
                searchText = "\\b" + searchText + "\\b";
            // 如果选择了区分大小写
            Pattern pattern;
            if(caseSensitive.isSelected())
                pattern= Pattern.compile(searchText);
            else
                pattern= Pattern.compile(searchText,Pattern.CASE_INSENSITIVE);
            、Matcher matcher = pattern.matcher(textArea.getSelectedText());
            System.out.println("searchText: "+initText+" "+textArea.getSelectedText());

            if ((!matcher.find())||!textArea.getSelectedText().equals(initText)) {
                System.out.println("没有找到匹配的文本!");
                boolean n= FindNext();
                boolean b=false;
                if(!n)b=FindBefore();
                System.out.println("next:"+n+" before:"+b);
                //if(!b&&!n)FindFirst();
                System.out.println("lastMatch:"+lastMatchStart+" "+lastMatchEnd);
                System.out.println("currentStart:"+currentStart);
                System.out.println();
            }

        }
        else {
            System.out.println("没有选中文本!");
            boolean n= FindNext();
            boolean b=false;
            if(!n)b=FindBefore();
            System.out.println("next:"+n+" before:"+b);
            //if(!b&&!n)FindFirst();
            System.out.println("lastMatch:"+lastMatchStart+" "+lastMatchEnd);
            System.out.println("currentStart:"+currentStart);
            System.out.println();
        }
    }


    public  void FindFirst()
    {

        String searchText = searchField.getText();

        // 如果选择了全词匹配
        if (Word.isSelected())
            searchText = "\\b" + searchText + "\\b";
        // 如果选择了区分大小写
        Pattern pattern;
        if(caseSensitive.isSelected())
            pattern= Pattern.compile(searchText);
        else
            pattern= Pattern.compile(searchText,Pattern.CASE_INSENSITIVE);
        。Matcher matcher = pattern.matcher(textArea.getText());

        //如果选中文本开启查找，则设置初值
        if (matcher.find()) {
            lastMatchStart = textArea.getSelection().getStart();
            lastMatchEnd = textArea.getSelection().getEnd();
            textArea.selectRange(lastMatchStart, lastMatchEnd);
            currentStart=lastMatchStart;
            //System.out.println("lastMatchStart: "+lastMatchStart);
            return;
        }


    }

 */