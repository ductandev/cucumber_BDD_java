Feature: User Registration

  Background: The user is on the registration page
    Given the user is on the registration page

  # Trường hợp: Đăng ký thành công
  Scenario: Successfully register with valid details
    When the user enters a valid email
    And I click the "인증 코드 요청" button
    And I click the "인증 확인" button to verify
    And the user enters a valid password, confirms password, and contactName
    And I click the "회원가입" button
    Then the user should see a message "성공적으로 등록되었습니다!"
    And be redirected to the login page

  # Trường hợp: Email không hợp lệ
  Scenario: Registration fails with invalid email
    When the user enters an invalid email address
    Then the user should see an error validate message "올바른 이메일 주소를 입력해주세요"


#  # Trường hợp: Mật khẩu không khớp
#  Scenario: Registration fails when passwords do not match
#    When the user enters a password and a confirmation password that do not match
#    And clicks the "회원가입" button
#    Then the user should see an error message "Passwords do not match"
#
#  # Trường hợp: Tên người dùng đã tồn tại
#  Scenario: Registration fails when username is already taken
#    When the user enters a username that already exists
#    And fills in other fields correctly
#    And clicks the "회원가입" button
#    Then the user should see an error message "Username already exists"
#
#  # Trường hợp: Bỏ trống các trường thông tin
#  Scenario: Registration fails when required fields are empty
#    When the user leaves one or more required fields empty
#    And clicks the "회원가입" button
#    Then the user should see an error message "All fields are required"
#
#  # Trường hợp: Mật khẩu không đủ mạnh
#  Scenario: Registration fails with weak password
#    When the user enters a password that does not meet complexity requirements
#    And clicks the "회원가입" button
#    Then the user should see an error message "Password must contain at least 8 characters, including uppercase, lowercase, and a number"
