package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public abstract class Report {
    protected Map<BikeCategory, Section> sections;

    public Report(Map<BikeCategory, Section> sections) {
        this.sections = sections;
    }

    public abstract List<String> generateReport(boolean confirm);
}
