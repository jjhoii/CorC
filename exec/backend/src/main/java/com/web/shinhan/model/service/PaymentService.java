package com.web.shinhan.model.service;

import com.web.shinhan.entity.Store;
import com.web.shinhan.entity.User;
import com.web.shinhan.model.TransactionDto;
import com.web.shinhan.model.UserDto;
import com.web.shinhan.repository.StoreRepository;
import com.web.shinhan.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.shinhan.entity.Payment;
import com.web.shinhan.model.PaymentDto;
import com.web.shinhan.model.mapper.PaymentMapper;
import com.web.shinhan.repository.PaymentRepository;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private BlockchainService blockchainService;

  @Autowired
  private UserService userService;

  private final PaymentMapper mapper = Mappers.getMapper(PaymentMapper.class);

  @Transactional
  public Page<PaymentDto> findUserPayment(int userId, Pageable pageable) {
    Page<Payment> payments = paymentRepository.findAllByUserId(userId, pageable);
    return payments.map(PaymentDto::of);
  }

  @Transactional
  public Page<PaymentDto> findAll(Pageable pageable) {
    Page<Payment> payments = paymentRepository.findAll(pageable);
    return payments.map(PaymentDto::of);
  }

  @Transactional
  public boolean confirmPayment(int storeId) {
    List<Payment> payment = paymentRepository.findByStoreId(storeId);
    for(Payment py : payment) {
    	if (py.getStatus() == 1) {
    		PaymentDto paymentDto = mapper.INSTANCE.paymentToDto(py);
    		paymentDto.setStatus(2);
    		paymentRepository.save(paymentDto.toEntity());
    	} else if(py.getStatus() == 2){
    		continue;
    	} else {
    		return false;
    	}
    }
    return true;
  }

  @Transactional
  public Page<PaymentDto> findStorePayment(int storeId, Pageable pageable) {
    Page<Payment> payments = paymentRepository.findAllByStoreId(storeId, pageable);
    return payments.map(PaymentDto::of);
  }

  public int findStoreTotal() {
    int total = 0;
    List<Integer> used = paymentRepository.calcTotalExpense();
    for (int nc : used) {
      total += nc;
    }
    return total;
  }

  @Transactional
  public List<PaymentDto> findAllByStatus() {
    List<Payment> payments = paymentRepository.findAllByStatus();
    List<PaymentDto> paymentDto = new ArrayList<>();
    for (Payment payment : payments) {
      PaymentDto dto = mapper.INSTANCE.paymentToDto(payment);
      paymentDto.add(dto);
    }
    return paymentDto;
  }

  public int calcTotalExpense() {
    int total = 0;
    List<Integer> used = paymentRepository.calcTotalExpense();
    for (int nc : used) {
      total += nc;
    }
    return total;
  }

  public int notConfirmed() {
    int total = 0;
    List<Integer> notConfirmed = paymentRepository.findTotalByStatus();
    for (int nc : notConfirmed) {
      total += nc;
    }
    return total;
  }

  public int expenseByMonth(int now, int year) {
    int monthly = 0;

    if (now == 1 || now == 3 || now == 5 || now == 7 || now == 8 || now == 10 || now == 12) {
      LocalDateTime startDate = LocalDateTime.of(year, now, 01, 00, 00);
      LocalDateTime endDate = LocalDateTime.of(year, now, 31, 23, 59);
      List<Integer> payments = paymentRepository.findAllByMonth(startDate, endDate);
      for (int payment : payments) {
        monthly += payment;
      }
    } else if (now == 4 || now == 6 || now == 9 || now == 11) {
      LocalDateTime startDate = LocalDateTime.of(year, now, 01, 00, 00);
      LocalDateTime endDate = LocalDateTime.of(year, now, 30, 23, 59);
      List<Integer> payments = paymentRepository.findAllByMonth(startDate, endDate);
      for (int payment : payments) {
        monthly += payment;
      }
    } else {
      if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
        LocalDateTime startDate = LocalDateTime.of(year, now, 01, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(year, now, 29, 23, 59);
        List<Integer> payments = paymentRepository.findAllByMonth(startDate, endDate);
        for (int payment : payments) {
          monthly += payment;
        }
      } else {
        LocalDateTime startDate = LocalDateTime.of(year, now, 01, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(year, now, 28, 23, 59);
        List<Integer> payments = paymentRepository.findAllByMonth(startDate, endDate);
        for (int payment : payments) {
          monthly += payment;
        }
      }
    }
    return monthly;
  }

  public int findTotal(int storeId) {
    int total = 0;
    List<Integer> totalUsed = paymentRepository.findTotalByStoreId(storeId);
    for (int nc : totalUsed) {
      total += nc;
    }
    return total;
  }

  public int findNotConfirmed(int storeId) {
    int total = 0;
    List<Integer> notConfirmed = paymentRepository.findNotConfirmedByStoreId(storeId);
    for (int nc : notConfirmed) {
      total += nc;
    }
    return total;
  }

  public int findUserPaymentCustom(int now, int year) {
    int monthly = 0;

    if (now == 1 || now == 3 || now == 5 || now == 7 || now == 8 || now == 10 || now == 12) {
      LocalDateTime startDate = LocalDateTime.of(year, now, 01, 00, 00);
      LocalDateTime endDate = LocalDateTime.of(year, now, 31, 23, 59);
      List<Integer> payments = paymentRepository.findAllByMonth(startDate, endDate);
      for (int payment : payments) {
        monthly += payment;
      }
    } else if (now == 4 || now == 6 || now == 9 || now == 11) {
      LocalDateTime startDate = LocalDateTime.of(year, now, 01, 00, 00);
      LocalDateTime endDate = LocalDateTime.of(year, now, 30, 23, 59);
      List<Integer> payments = paymentRepository.findAllByMonth(startDate, endDate);
      for (int payment : payments) {
        monthly += payment;
      }
    } else {
      if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
        LocalDateTime startDate = LocalDateTime.of(year, now, 01, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(year, now, 29, 23, 59);
        List<Integer> payments = paymentRepository.findAllByMonth(startDate, endDate);
        for (int payment : payments) {
          monthly += payment;
        }
      } else {
        LocalDateTime startDate = LocalDateTime.of(year, now, 01, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(year, now, 28, 23, 59);
        List<Integer> payments = paymentRepository.findAllByMonth(startDate, endDate);
        for (int payment : payments) {
          monthly += payment;
        }
      }
    }
    return monthly;
  }

  public Page<PaymentDto> findUserPaymentCustom(int userId, Pageable pageable, int startDate,
      int endDate) {
    int startYear = startDate / 10000;
    int startMonth = (startDate - startYear * 10000) / 100;
    int startDay = (startDate - startYear * 10000) % 100;
    int endYear = endDate / 10000;
    int endMonth = (endDate - endYear * 10000) / 100;
    int endDay = (endDate - endYear * 10000) % 100;
    LocalDateTime startDateIn = LocalDateTime.of(startYear, startMonth, startDay, 00, 00);
    LocalDateTime endDateIn = LocalDateTime.of(endYear, endMonth, endDay, 23, 59);
    Page<Payment> payments = paymentRepository
        .findAllByCustom(userId, pageable, startDateIn, endDateIn);
    return payments.map(PaymentDto::of);
  }

  public void pay(int userId, int storeId, int bill) {
    PaymentDto paymentDto = new PaymentDto();
    paymentDto.setDate(LocalDateTime.now());
    paymentDto.setUserId(userId);
    paymentDto.setStoreId(storeId);
    paymentDto.setTotal(bill);
    paymentDto.setStatus(1);
    Payment payment = paymentDto.toEntity();
    paymentRepository.save(payment);
    paymentDto.setPaymentId(payment.getPaymentId());

    // 블록체인 삽입
    // ISSUE: transaction이 생성되기 직전에 user의 balance가 수정되어 WorldState 데이터 접근 불가
    createBlockTransaction(paymentDto);
  }

  public PaymentDto findLastPayment() {
    Payment payment = paymentRepository.findTop1ByOrderByPaymentIdDesc();
    PaymentDto dto = mapper.INSTANCE.paymentToDto(payment);
    return dto;
  }

  public PaymentDto findPayment(int paymentId) {
    Payment paymentEN = paymentRepository.findByPaymentId(paymentId);
    PaymentDto dto = mapper.INSTANCE.paymentToDto(paymentEN);
    return dto;
  }

  public List<Integer[]> calcNotConfirmed() {
    List<Integer[]> notConfirmed = paymentRepository.findTotalByStatusandStoreId();
    return notConfirmed;
  }

  public int countStorePayment(int storeId) {
    int count = paymentRepository.countStorePayment(storeId);
    return count;
  }

  public int countPayment() {
    int count = (int) paymentRepository.count();
    return 0;
  }

  public int countUserPayment(int userId) {
    int count = paymentRepository.countUserPayment(userId);
    return 0;
  }

	public Page<PaymentDto> findStorePaymentCustom(int storeId, Pageable pageable, int startDate, int endDate) {
		int startYear = startDate / 10000;
		int startMonth = (startDate - startYear * 10000) / 100;
		int startDay = (startDate - startYear * 10000) % 100;
		int endYear = endDate / 10000;
		int endMonth = (endDate - endYear * 10000) / 100;
		int endDay = (endDate - endYear * 10000) % 100;
		LocalDateTime startDateIn = LocalDateTime.of(startYear, startMonth, startDay, 00, 00);
		LocalDateTime endDateIn = LocalDateTime.of(endYear, endMonth, endDay, 23, 59);
		
		Page<Payment> payments = paymentRepository.findAllByStoreCustom(storeId, pageable, startDateIn, endDateIn);
		return payments.map(PaymentDto::of);
	}

  public boolean verifyBlockTransaction(PaymentDto payment) {
    try {
      TransactionDto tx = blockchainService.getTransaction(payment.getTransactionId()).block();
      if (payment.getUser().getEmail().equals(tx.getFrom()) &&
      payment.getStore().getEmail().equals(tx.getTo()) &&
      payment.getTotal() == tx.getValue()) {
        payment.setVerified(true);
      }

      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  public void createBlockTransaction(PaymentDto payment) {
    System.out.println("test1");
    User user = userRepository.findByUserId(payment.getUserId());
    Store store = storeRepository.findByStoreId(payment.getStoreId());
    TransactionDto tx = TransactionDto.builder()
        .from(user.getEmail())
        .to(store.getEmail())
        .value(payment.getTotal())
        .build();

    Mono<TransactionDto> u = blockchainService.createTransaction(tx);
    u.subscribe(response -> {
      System.out.println("test2");
      // 생성된 경우 상태 변경
      payment.setTransactionId(response.getTxId());
      payment.setTestCode(1);
      paymentRepository.save(payment.toEntity());

      // user write 이후 read 불가능하기 때문에 transaction 생성 이후 user write
      userService.setBlockUserBalance(UserDto.of(user));
    });
  }
}