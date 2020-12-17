package com.github.piasy.mediacodecrctest.chart;


import java.util.ArrayList;
import java.util.List;

public class EchartOptionUtil {

    public static OptionBean getLineChartOptions(List<String> typeTitles,List<String> time,List<List<Double>> datas) {
        OptionBean option = new OptionBean();
        OptionBean.Title title=new OptionBean.Title();
        title.setText("");
        title.setLeft("center");
        option.setTitle(title);
        OptionBean.ToolTip toolTip=new OptionBean.ToolTip();
        toolTip.setTrigger("item");
        toolTip.setFormatter("{a} <br/>{b} : {c}");
        option.setTooltip(toolTip);
        OptionBean.Legend legend=new OptionBean.Legend();
        legend.setLeft("left");
        legend.setData(typeTitles);
        option.setLegend(legend);
        OptionBean.XAxis xAxis=new OptionBean.XAxis();
        xAxis.setType("category");
        xAxis.setName("时间");
        OptionBean.SplitLine splitLine=new OptionBean.SplitLine();
        splitLine.setShow(false);
        xAxis.setSplitLine(splitLine);
        xAxis.setData(time);
        option.setxAxis(xAxis);
        OptionBean.Grid grid=new OptionBean.Grid();
        grid.setLeft("0%");
        grid.setRight("4%");
        grid.setBottom("3%");
        grid.setContainLabel(true);
        option.setGrid(grid);
        OptionBean.YAxis yAxis=new OptionBean.YAxis();
        yAxis.setType("log");
        yAxis.setName("码率");
        OptionBean.SplitLine splitLine1=new OptionBean.SplitLine();
        splitLine.setShow(true);
        yAxis.setMinorSplitLine(splitLine1);
        yAxis.setMinorTick(splitLine1);
        option.setyAxis(yAxis);
        List<OptionBean.Series> seriesList=new ArrayList<>();
        for(int i=0;i<typeTitles.size();i++){
            OptionBean.Series series=new OptionBean.Series();
            series.setName(typeTitles.get(i));
            series.setType("line");
            series.setData(datas.get(i));
            seriesList.add(series);
        }
        option.setSeries(seriesList);
        return option;
    }


}