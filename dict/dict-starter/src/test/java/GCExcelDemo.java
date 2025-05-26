import com.grapecity.documents.excel.IWorksheet;
import com.grapecity.documents.excel.Workbook;

public class GCExcelDemo {
    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            Object[][] sourceData = new Object[][]{
                    {"ITEM", "AMOUNT"},
                    {"Income 1", 2500},
                    {"Income 2", 1000},
                    {"Income 3", 250},
                    {"Other", 250},
            };

            Object[][] sourceData1 = new Object[][]{
                    {"ITEM", "AMOUNT"},
                    {"Rent/mortgage", 800},
                    {"Electricity", 120},
                    {"Gas", 50},
                    {"Cell phone", 45},
                    {"Groceries", 500},
                    {"Car payment", 273},
                    {"Auto expenses", 120},
                    {"Student loans", 50},
                    {"Credit cards", 100},
                    {"Auto insurance", 78},
                    {"Personal care", 50},
                    {"Entertainment", 100},
                    {"Miscellaneous", 50},
            };


            Workbook workbook = new Workbook();
            IWorksheet worksheet = workbook.getWorksheets().get(0);
            worksheet.getRange("B3:C7").setValue(sourceData);
            worksheet.getRange("B10:C23").setValue(sourceData1);
            worksheet.setName("Tables");

            worksheet.getRange("B2:C2").merge();
            worksheet.getRange("B2").setValue("MONTHLY INCOME");
            worksheet.getRange("B9:C9").merge();
            worksheet.getRange("B9").setValue("MONTHLY EXPENSES");
            worksheet.getRange("E2:G2").merge();
            worksheet.getRange("E2").setValue("PERCENTAGE OF INCOME SPENT");
            worksheet.getRange("E5:G5").merge();
            worksheet.getRange("E5").setValue("SUMMARY");
            worksheet.getRange("E3:F3").merge();
            worksheet.getRange("E9").setValue("BALANCE");
            worksheet.getRange("E6").setValue("Total Monthly Income");
            worksheet.getRange("E7").setValue("Total Monthly Expenses");

            String name = String.format("%03d", i + 1);

            workbook.save("./GcExcelFeatures-" + name + ".xlsx");
        }

    }
}
