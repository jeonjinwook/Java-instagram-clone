package com.Java_instagram_clone.domain.profile.service;

import com.Java_instagram_clone.domain.auth.entity.Response;
import com.Java_instagram_clone.domain.profile.entity.Profile;
import com.Java_instagram_clone.domain.profile.entity.RequestProfile;
import com.Java_instagram_clone.domain.profile.entity.ResponseProfile;
import com.Java_instagram_clone.domain.profile.repository.ProfileRepository;
import com.Java_instagram_clone.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final FileService fileService;
    private final Response responseDto;

    public ResponseEntity<?> findProfileById(RequestProfile profile) {

        long userNo = profile.getUserNo();

        ResponseProfile memberProfile = profileRepository.findProfileById(userNo);

        return responseDto.success(memberProfile, "정상적으로 조회했습니다.", HttpStatus.OK);

    }

    public ResponseEntity<?> create(MultipartFile[] uploadFiles) {

        ArrayList<String> createFiles = new ArrayList<>();

        try {

            if (uploadFiles != null) {
                createFiles = fileService.uploadFile(uploadFiles);
            }

            if (!createFiles.isEmpty()) {

                for (String file : createFiles) {

                    Profile profile = Profile.builder()
                            .photo(file)
                            .gender("")
                            .birthday("")
                            .build();
                    profileRepository.save(profile);

                }

            }
        } catch (Exception e) {
            return responseDto.fail("시스템 오류입니다. 관리자에게 문의하세요", HttpStatus.NOT_FOUND);
        }

        return responseDto.success("정상적으로 프로필 업데이트 했습니다.");
    }


//    async create(profile: ProfilesDTO, photo: Express.Multer.File) {
//        if (photo !== undefined || null) {
//      const uploadedFile: string = this.filesService.uploadFile(photo);
//            profile.photo = uploadedFile;
//        }
//    const user = await this.userRepository.findOne({ id: profile.userId });
//    const data = await this.profileRepository.create(profile);
//        data.user = user;
//
//        await this.profileRepository.save(data);
//
//        return profile;
//    }
//
//    async findAll() {
//    const data = await this.profileRepository.find({ relations: ['user'] });
//    const result = data.map((data) => {
//      const { password, refreshToken, ...userData } = data.user;
//      const { id, gender, birthday } = data;
//
//        return { id, user: userData, gender, birthday };
//    });
//        return result;
//    }
//
//    async findByUserId(userId: string) {
//    const user = await this.userRepository.findOne({ id: userId });
//    const data = await this.profileRepository.findOne({
//                where: { user },
//        relations: ['user'],
//    });
//
//    const { password, refreshToken, ...userData } = data.user;
//    const { id, gender, birthday } = data;
//
//        return { id, user: userData, gender, birthday };
//    }
//
//    async update(profile: ProfilesDTO, photo: Express.Multer.File) {
//    const user = await this.userRepository.findOne({ id: profile.userId });
//    const existingProfile = await this.profileRepository.findOne({ user });
//
//        if (existingProfile) {
//            await this.remove(existingProfile.id);
//        }
//
//    const data = await this.create(profile, photo);
//
//        return data;
//    }
//
//    async remove(id: string) {
//    const existingProfile = await this.profileRepository.findOne({ id });
//        await this.filesService.removeFile(existingProfile.photo);
//
//    const data = await this.profileRepository.delete({ id });
//
//        return id;
//    }
//
//    async removeByUserId(id: string) {
//    const user = await this.userRepository.findOne({ id });
//    const existingProfile = await this.profileRepository.findOne({ user });
//        await this.filesService.removeFile(existingProfile.photo);
//
//    const data = await this.profileRepository.delete({ id });
//
//        return id;
//    }


}
