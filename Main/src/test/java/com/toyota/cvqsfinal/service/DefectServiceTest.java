package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.GetDefectParameters;
import com.toyota.cvqsfinal.dto.ImageDto;
import com.toyota.cvqsfinal.entity.Defect;
import com.toyota.cvqsfinal.entity.Image;
import com.toyota.cvqsfinal.repository.DefectRepository;
import com.toyota.cvqsfinal.repository.ImageRepository;
import com.toyota.cvqsfinal.utility.DtoConvert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

class DefectServiceTest {

     @Mock
     private ImageRepository imageRepository;

     @Mock
     private DefectRepository defectRepository;

     @Spy
     private DtoConvert dtoConvert;


    private DefectService defectService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        defectService = new DefectService(imageRepository, defectRepository, dtoConvert);
    }
    @Test
    void testDefectSave() {

        ImageDto imageDto = ImageDto.builder().data("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").name("image").build();

        DefectDto defectDto = DefectDto.builder().name("testDefect").imageDto(imageDto).build();
        defectService.defectSave(defectDto);
        Defect defect = dtoConvert.defectDtoToDefect(defectDto);
        Mockito.verify(defectRepository, Mockito.times(1)).save(defect);

    }

    @Test
    void testDefectDelete() {
        Image image = Image.builder().id(1L).data(new byte[5]).deleted(false).build();
        Defect defect = Defect.builder().id(1L).defectName("testDefect").deleted(false).image(image).build();
        Mockito.when(defectRepository.getDefectByIdAndDeletedFalse(1L)).thenReturn(defect);
        defectService.defectDelete(1L);
        defect.setDeleted(true);
        image.setDeleted(true);
        Mockito.verify(defectRepository, Mockito.times(1)).save(defect);
        Mockito.verify(imageRepository, Mockito.times(1)).save(image);
    }

    @Test
    void testDefectGet() {
        Image image = Image.builder().id(1L).data(new byte[5]).deleted(false).build();
        Defect defect = Defect.builder().id(1L).defectName("testDefect").deleted(false).image(image).build();
        Mockito.when(defectRepository.getDefectByIdAndDeletedFalse(1L)).thenReturn(defect);
        DefectDto defectDto = defectService.defectGet(1L);
        assertEquals(defectDto.getName(), defect.getDefectName());
        assertEquals(defectDto.getImageDto().getId(), defect.getImage().getId());
    }


    @Test
    void testUpdateDefect() {
        ImageDto imageDto = ImageDto.builder().data("/9j/4AAQSkZJRgABAQEAYABgAAD//gBaRmlsZSBzb3VyY2U6IGh0dHBzOi8vY29tbW9ucy53aWtpbWVkaWEub3JnL3dpa2kvRmlsZTpKUEVHX2V4YW1wbGVfaW1hZ2VfZGVjb21wcmVzc2VkLmpwZ//bAEMABgQFBgUEBgYFBgcHBggKEAoKCQkKFA4PDBAXFBgYFxQWFhodJR8aGyMcFhYgLCAjJicpKikZHy0wLSgwJSgpKP/AAAsIALQAtAEBIgD/xAAcAAACAgMBAQAAAAAAAAAAAAAABQQGAQMHAgj/xABLEAABAwECBA8LCwQDAQAAAAAAAQIDBAURBhRRkRITITEyMzQ1UlNxcnOxsgcVNkFUYXSSk7PRFiIjJEJDYoGCotIloaPBF1WDRP/aAAgBAQAAPwD57A9R7Y3lN4Ea0dobzk6lF4G2DWdymwB7Zm4Iv1dpSUZbskETtm7nL1mDDti7kIwDfBXfpnRSdSFxADnwHqPbG8pvAjWjtDecnUovA2wazuU2APbM3BF+rtKSjLdkgids3c5eswYdsXchGAb4K79M6KTqQuIAc702PhtDTY+G0yyaJHIqyNRE8Ztxqn46POGNU/HR5zRWzxSQokcjXLor7k5FIV6ZQvTKTrOo6qrjkdS08kzWuRHKxNZbiV3otLyCozJ8Q70Wl5BUZk+JZLHwdtqWzIHx2TWPYuiucjUuX5y+cl/Jq3f+nrfVT4npuDNu6JP6PW6/BT4lafYtqJI9Fs+pvRypsUy8pjvNaf8A19T6qfE8TWTaMcMj5KGoaxjVc5yolyIiaq64q0K5A0K5Bpg05sVrtfKqMZpT0vXW1kLZjlNx8ecMcpuPjzhjlNx8ec5qBh2xU1XheCa56AuGAu4q7pmdkstwXF/wVRPk9R6nD7bhrcmQ9RommM1PGhzuVPppekd2lPNxFtVP6TX+jydlTnCayASbO3WnNUa3heF5UQMO2KmkDLdc9AXDATcVd0zOyWUC/wCCvg9R/r7bhqeo9sZzkOeS7dL0ju0p4I1q701/o8nZU5umsgEmzt1pzVGgAVO4LjzL82Nzl1kS8iYwzI7MGMMyOzG2mckz1azUVEv1SRpDsrc4aQ7K3OdB7mFgVdp2bab6eSnakdRG1dMc5NVWKupcily+R1o8fReu/wDiHyOtHj6L13/xOk4HYAWtNgzQyMqbORq6ZdfI+/bHfgHP/HlseVWZ7ST+BmPueWxpjPrVm66feSfwOWz4IWilRMmn0WpI9Nm/hL+E8fJG0ePovXf/ABIVuYLV8FhWnM+ajVsdJM9Ua919yMVdT5px9Kd9yarc5nF35W51NtM1YJkkfcqXKnzdfVJeNMyOzBjTMjswY0zI7MVwDVU7nk5P9ioCbZW6H8xetBmB17uH7zW16ZF7pTpAHWsA/A+zv/X3rx8eottj5ydZxOp3VUdNJ21NYtwo8Frc9AqPduPndNZDJ5k2JqABSBqqdzycn+xUBNsrdD+YvWgzA693D95ra9Mi90p0gDrWAfgfZ3/r714+PUW2x85Os4nU7qqOmk7amsW4UeC1uegVHu3Hzumshk8ybE1ABFxJvGPzIGJN4x+ZDVV0bW0sy6Y5bm36yCjFm8JcwYs3hLmJtk0zVqXpo3bBfF50GmKN4bswYo3huzHYe4VRNfYtuXyOS6siTWTilOld72ca/Mgd72ca/Mh1XASjb8k6BqSO1NM8ScY4fYknGOzIem0iNc12jctyoushwmqS6rqUyTSJ+9TULcKPBa2/QKj3bj54RNRDNwIxHroVVU5DOLN4bswYs3huzBizeG7MRgNNbuObmCZdcwTrI3U/o160GwHY+4PvLbvpsXulOmAdSwE8FaLlk944fgpwCr3bVdPL23GoW4UeC1t+gVHu3HzymsgHuLZ/kbQAXgaa3cc3MEy65gnWRup/Rr1oNgOx9wfeW3fTYvdKdMA6lgJ4K0XLJ7xw/BTgFXu2q6eXtuNQtwo8Frb9AqPduPnlNZAPcWz/ACNoAa9Jj4P91DSY+D/dTXVQRLSy3s1NDlUVYrBxf7lDFYOL/cpvo4I45XKxty6G7XXKhMuQLkOkdyitqaOzLVbTS6W19TGqpoUW9dL86F3782j5T/jb8A782j5T/jb8C84MYR2vFYNKyOtVrU0eppTOG78I0+U9teXr7KP+J6iwmtp0rEWvW5XIi/RR5eacQqsI7XWrqVxz76T7mPhr+E1fKK1/LP8ADH/Eh23b9qyWHacclXex9LK1yaUxL0Vi+Y5ClTNcnz/7IGMzcP8AshPsR7qi0WxzLomKxy3XXaqJ5h/ikHF/uUMUg4v9yhikHF/uUVga6nc0vNFoG2m2xeQkAX7uZ73Wn6Qz3ZcQLng7vJS/r7ajE9w7dHzk6ziNTuqp6aTtqayJa+89oejS9lTl6ayAM8Hd9W9G/qQtAAJANdVuaXmiy9MqBemVDbTKiyLdkJNwXHQO5ixzrOtPQtcv1hmsl/3ZcdKk4t/qqGlScW/1VLpg7DL3kpfopPt/ZXhqMdJl4qT1VPcMMunR/RSbJPsrlOIVMb8bqfmP26T7K8NTXpb+A/1VIlsMeljWhex25pfEvAU5aiLcmoZuXIMsHd9W9G/qQtF4XheKLguC5MiGNC3gtzIGhbwW5kI9ciJClyInzvEl3iUhXrlXOF65VzkygkkYyRGSysRXJfoXq2/U8ykrT5vKKj2zviGnzeUVHtnfEdWdUVCUMSJVVSJq6078q+ck4zU+VVXt3/EyypqdG361Va6f/Q/4lDlll06X6afZu+9dlXznnTZeOm9q74niaWVYZEWaZUVqoqLI7Jyiu9cq5wvXKucdYIatusv1foZNfkQu/wCSZg/JMwfkmYr4AAEeu2lvO/0pBAlUWwfyoSAHNnbii/PrUkmWbNvKhRZdtk57utTyeZdqfzVFoDrA/f1nQydSF3ABDoHcF2YNA7guzAjHqqIjHKq6yIinvF5uJl9RQxebiZfUUi2jFKyBqvikamiRL1aqeJRfoXcFcwaF3BXMbqeWOFrkmkZGqreiPcjb85txum8pg9o34hjdN5TB7RvxHNn1lLiUX1qn8f3iZV85Ixyl8qp/aN+JllZS6Nt9VT66feN+JTZEVZZFRFVFe5UW7zqedC7grmPMrHLG9Ea5V0K6lxA0ibiZfUUNIm4mX1FHOCMUjbcYro3tTSZNVWqniQuty5FzBcuRcwXLkXMKgNtLumLnDMyLMINwx9KnZcIAE9u7dBzF6xYA2oNxx/n1qbwN6ayAZTXQ2AMMH9829G/qLMACQDbS7pi5wzAW4Qbhj6VOy4QAJ7d26DmL1iwBtQbjj/PrUkAbk1kAymuh7AY4P76N6N/UWYAFeLS5G5wxaXI3ObaOkmdVwtRG3q7U+cOO9tTwY/aIHe2p4MftEFeEdn1DKCNXJHdpyJqP/C4r2JzZGesGJzZGesI8I4nxT06Pu1Y3Kly3/aFADqzonuoolS65b/H51JGkSZG5wWCREvubnGTbHrFaio2K5URdtQz3nrODD7VASx6y/Yw+1Q9d6avgxe0QO9NXwYvaITrGs6phtBr5Ej0Ogcmo+/xD7SX5G5w0mTI3OGkyZG5yIBJs7fCm56dSliTWAT4U72xdMnZcVgCtYX7ppOid2hABYbJ3uh/V2lJZh2xdyKWyPa2c1vUhkAA30e3pyKTQAVgSbO3wpuenUpYk1gE+FO9sXTJ2XFYArWF+6aTondoQAWGyd7of1dpSWYdsXcilsj2tnNb1IZAAN9Ht6cik0AFYEmzt8Kbnp1KWJNYBPhTvbF0ydlxWAK1hfumk6J3aEAFhsne6H9XaUlmHbF3IpbI9rZzW9SGQADfR7enIpNAD/9k=").type("jpg").name("image").build();

        DefectDto defectDto = DefectDto.builder().id(1L).name("testDefect").imageDto(imageDto).build();

        Defect defect = dtoConvert.defectDtoToDefect(defectDto);
        Mockito.when(defectRepository.getDefectByIdAndDeletedFalse(1L)).thenReturn(defect);
        Mockito.when(defectRepository.save(defect)).thenReturn(defect);
        Mockito.when(imageRepository.save(defect.getImage())).thenReturn(defect.getImage());
        defectDto.setName("testDefect2");
        DefectDto defectDto1 = defectService.updateDefect(defectDto);


        assertEquals(defectDto1.getName(), defectDto.getName());
        assertEquals(defectDto1.getImageDto().getName(), defectDto.getImageDto().getName());
        assertEquals(defectDto1.getImageDto().getType(), defectDto.getImageDto().getType());

    }

    @Test
    void testGetDefectsWithPagination() {

        GetDefectParameters parameters = GetDefectParameters.builder().page(0).pageSize(10).filterKeyword("").sortType("ASC").build();

        Pageable pageable = PageRequest.of(parameters.getPage(), parameters.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));

        Mockito.when(defectRepository.getAllByDefectNameLikeAndDeletedFalse(  "%%",pageable)).thenReturn(null);
        try {
            defectService.getDefectsWithPagination(parameters);
        }
        catch (Exception e){

        }
        Mockito.verify(defectRepository, Mockito.times(1)).getAllByDefectNameLikeAndDeletedFalse(  "%%",pageable);
    }
}