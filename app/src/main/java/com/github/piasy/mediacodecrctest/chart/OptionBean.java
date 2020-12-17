package com.github.piasy.mediacodecrctest.chart;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public class OptionBean {

    public static class Title{
        private String text;
        private String left;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }
    }

    public static class ToolTip{
        private String trigger;
        private String formatter;

        public String getTrigger() {
            return trigger;
        }

        public void setTrigger(String trigger) {
            this.trigger = trigger;
        }

        public String getFormatter() {
            return formatter;
        }

        public void setFormatter(String formatter) {
            this.formatter = formatter;
        }
    }

    public static class Legend{
        private String left;
        private List<String> data;

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }

    public static class XAxis{
        private String type;
        private String name;
        private SplitLine splitLine;
        private List<String> data;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SplitLine getSplitLine() {
            return splitLine;
        }

        public void setSplitLine(SplitLine splitLine) {
            this.splitLine = splitLine;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }

    public static class SplitLine{
        private boolean show;

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }
    }

    public static class Grid{
        private String left;
        private String right;
        private String bottom;
        private boolean containLabel;

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        public String getRight() {
            return right;
        }

        public void setRight(String right) {
            this.right = right;
        }

        public String getBottom() {
            return bottom;
        }

        public void setBottom(String bottom) {
            this.bottom = bottom;
        }

        public boolean isContainLabel() {
            return containLabel;
        }

        public void setContainLabel(boolean containLabel) {
            this.containLabel = containLabel;
        }
    }

    public static class YAxis{
        private String type;
        private String name;
        private SplitLine minorTick;
        private SplitLine minorSplitLine;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SplitLine getMinorTick() {
            return minorTick;
        }

        public void setMinorTick(SplitLine minorTick) {
            this.minorTick = minorTick;
        }

        public SplitLine getMinorSplitLine() {
            return minorSplitLine;
        }

        public void setMinorSplitLine(SplitLine minorSplitLine) {
            this.minorSplitLine = minorSplitLine;
        }
    }

    public static class Series{
        private String type;
        private String name;
        private List<Double> data;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Double> getData() {
            return data;
        }

        public void setData(List<Double> data) {
            this.data = data;
        }
    }

    private Title title;
    private ToolTip tooltip;
    private Legend legend;
    private XAxis xAxis;
    private Grid grid;
    private YAxis yAxis;
    private List<Series> series;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public ToolTip getTooltip() {
        return tooltip;
    }

    public void setTooltip(ToolTip tooltip) {
        this.tooltip = tooltip;
    }

    public Legend getLegend() {
        return legend;
    }

    public void setLegend(Legend legend) {
        this.legend = legend;
    }

    public XAxis getxAxis() {
        return xAxis;
    }

    public void setxAxis(XAxis xAxis) {
        this.xAxis = xAxis;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public YAxis getyAxis() {
        return yAxis;
    }

    public void setyAxis(YAxis yAxis) {
        this.yAxis = yAxis;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    @Override
    /**
     * 获取toString值
     */
    public String toString() {
        return GsonUtil.format(this);
    }
}
