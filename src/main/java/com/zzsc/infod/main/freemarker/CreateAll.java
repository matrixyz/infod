package com.zzsc.infod.main.freemarker;

public class CreateAll {
    public static void main(String[] args) {
        String table = "endowment";
        try {
             CreateController.main(new String[]{table});
            CreateMapper.main(new String[]{table});
            CreateMapping.main(new String[]{table});
           CreateModel.main(new String[]{table});
           CreateModelDto.main(new String[]{table});
             CreateVForm.main(new String[]{table});
            CreateVList.main(new String[]{table});

           CreateService.main(new String[]{table});
            CreateServiceImpl.main(new String[]{table});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
