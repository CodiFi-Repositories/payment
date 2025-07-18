package in.codifi.ambalal.controller;

import in.codifi.ambalal.controller.spec.BaseAdminController;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.ambalal.model.StatusRequest;
import in.codifi.ambalal.service.spec.BaseAdminService;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.EkycEndpointConstants;
import in.codifi.ambalal.model.PaymentStatusRequest;
import in.codifi.ambalal.model.PaymentStatusResponse;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.api.utilities.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Path;

@Path("/admin")
public class CustomAdminController implements BaseAdminController {

    @Inject
    BaseAdminService baseAdminService;
    
	@Inject
	ErrorHandling errorHandling;
    
    

    @Override
    public Response getStatusByDate(StatusRequest request) {
        return baseAdminService.getStatusByDate(request.getDate());
    }
    
    @Override
    public Response getPaymentStatus(PaymentStatusRequest request) {
        return Response.ok(baseAdminService.getPaymentStatus(request)).build();
    }
    

    @Override
    public Response downloadPaymentStatusExcel(PaymentStatusRequest request) {
        try {
            ResponseModel model = baseAdminService.getPaymentStatus(request);

            if (model.getResult() == null || !(model.getResult() instanceof List)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No data found to export")
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }

            @SuppressWarnings("unchecked")
            List<PaymentStatusResponse> data = (List<PaymentStatusResponse>) model.getResult();

            if (data.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No data found to export").type(MediaType.TEXT_PLAIN).build();
            }

            // Create Excel workbook
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Payment Status");

                // Header styling
                CellStyle headerStyle = workbook.createCellStyle();
                XSSFFont font = ((XSSFWorkbook) workbook).createFont();
                font.setBold(true);
                font.setColor(IndexedColors.WHITE.getIndex());
                headerStyle.setFont(font);
                headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                String[] headers = {
                        "Date", "User ID", "Transaction ID", "Amount",
                        "Segment", "Payment Method", "Globe", "TechExcel","RMS"
                };

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }
                // Data rows
                int rowNum = 1;
                for (PaymentStatusResponse record : data) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(record.getDate());
                    row.createCell(1).setCellValue(record.getUserId());
                    row.createCell(2).setCellValue(record.getTransactionId());
                    row.createCell(3).setCellValue(record.getAmount());
                    row.createCell(4).setCellValue(record.getSegment());
                    row.createCell(5).setCellValue(record.getPaymentMethod());
                    row.createCell(6).setCellValue(record.getGlobe());
                    row.createCell(7).setCellValue(record.getTechExcel());
                    row.createCell(8).setCellValue(record.getRms());

                }

                for (int i = 0; i <= 8; i++) {
                    sheet.autoSizeColumn(i);
                }

                workbook.write(out);
            }

            byte[] excelBytes = out.toByteArray();

            return Response.ok(excelBytes).header("Content-Disposition", "attachment; filename=payment_status.xlsx").build();

        } catch (Exception e) {
            e.printStackTrace();
            
            errorHandling.handleErrors("", EkycEndpointConstants.DOWNLOAD_PAYMENT_STATUS, MessageConstants.MODULE,
					ErrorCodeConstants.EC015, EkycConstants.INTERNAL_ERR, EkycConstants.DOWNLOAD_PAYMENT_STATUS,
					EkycConstants.ADMIN_CLASS, e.getMessage(), ErrorMessageConstants.FETCH_FAILED_TRANSACTIONS);
            
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to generate Excel").type(MediaType.TEXT_PLAIN).build();
        }
    }
}
