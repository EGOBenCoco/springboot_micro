
package com.example.plannerentity.global_exception;

import java.util.List;


public record ValidationErrorResponse(List<Violation> violations) { }