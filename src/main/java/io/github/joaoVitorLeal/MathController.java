package io.github.joaoVitorLeal;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/math-operations")
public class MathController {
	
	private final AtomicLong counter = new AtomicLong();
	
	@GetMapping("/sum/{firstNumber}/{secondNumber}")
	public Double sum(@PathVariable String firstNumber, @PathVariable String secondNumber) throws Exception {
		
		if (!isNumeric(firstNumber) || !isNumeric(secondNumber)) {
			throw new Exception();
		}
		
		return convertToDouble(firstNumber) + convertToDouble(secondNumber);
	}

	private Double convertToDouble(String strNumber) {
		if (strNumber == null) {
			//throw new NullPointerException("The number must not be null.");
			return 0D;
		}
		String number = strNumber.replaceAll(",", ".");
		if (isNumeric(number)) {
			return Double.parseDouble(number);
		}
		return 0D;
	}

	private boolean isNumeric(String strNumber) {
		if (strNumber == null) {
			//throw new NullPointerException("The number must not be null.");
			return false;
		}
		String number = strNumber.replaceAll(",", ".");
		return number.matches("[-+]?[0-9]*\\.?[0-9]+");
	}
}
