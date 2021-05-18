export const GetIcon = (categoryCode) => {
  let iconInfo = {};
  switch (categoryCode) {
    case '10302':
      // 과실 및 그 외 채소 절임식품 제조업
      iconInfo.iconName = 'fruit-cherries';
      iconInfo.family = 'MaterialCommunityIcons';
      break;
    case '10502':
      // 아이스크림 및 기타 식용 빙과류 제조업
      iconInfo.iconName = 'icecream';
      iconInfo.family = 'MaterialIcons';
      break;
    case '47121':
      // 슈퍼마켓
      iconInfo.iconName = 'store';
      iconInfo.family = 'StoreIcon';
      break;
    case '47122':
      // 체인화 편의점
      iconInfo.iconName = 'store';
      iconInfo.family = 'StoreIcon';
      break;
    case '55101':
      // 호텔업
      iconInfo.iconName = 'hotel';
      iconInfo.family = 'FontAwesome5';
      break;
    case '56111':
      // 한식 일반 음식점업
      iconInfo.iconName = 'restaurant-outline';
      iconInfo.family = 'Ionicons';
      break;
    case '56221':
      // 커피 전문점
      iconInfo.iconName = 'coffee';
      iconInfo.family = 'CoffeeIcon';
      break;

    default:
      // 기본 : 슈퍼마켓 아이콘
      iconInfo.iconName = 'store';
      iconInfo.family = 'StoreIcon';
      break;
  }
  return iconInfo;
};
