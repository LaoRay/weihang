package cn.com.clubank.weihang.common.sms;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MSEntity {

	private MSKey Key;

	private List<MSValue> MsList;
}
